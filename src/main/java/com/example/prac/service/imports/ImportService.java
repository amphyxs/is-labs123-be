package com.example.prac.service.imports;

import java.io.InputStream;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import com.example.prac.dto.data.DragonDTO;
import com.example.prac.dto.imports.ImportHistoryItemDTO;
import com.example.prac.exceptions.TransactionResourceIsNotReady;
import com.example.prac.mappers.impl.ImportHistoryItemMapper;
import com.example.prac.model.auth.Role;
import com.example.prac.model.auth.User;
import com.example.prac.model.imports.ImportHistoryItem;
import com.example.prac.model.imports.ImportStatus;
import com.example.prac.repository.auth.UserRepository;
import com.example.prac.repository.imports.ImportHistoryRepository;
import com.example.prac.service.auth.AuthenticationService;
import com.example.prac.service.auth.JwtService;
import com.example.prac.service.data.DragonService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportService {
    private final ImportHistoryRepository importHistoryRepository;
    private final DragonService dragonService;
    private final ObjectMapper objectMapper;
    private final AuthenticationService authenticationService;
    private final ImportHistoryItemMapper dtoMapper;
    private final MinioClient minioClient;
    private final PlatformTransactionManager transactionManager;
    private final Environment env;
    private final UserRepository userRepository;

    public String getImportsMinioBucketName() {
        return env.getProperty("minio.bucket-name");
    }

    public List<ImportHistoryItemDTO> getAllImportHistoryItems() {
        List<ImportHistoryItemDTO> history = importHistoryRepository.findAll().stream()
                .map(dtoMapper::mapTo).toList();
        if (authenticationService.getCurrentUser().getRole() == Role.ADMIN) {
            return history;
        }
        return history.stream()
                .filter(item -> item.getOwner().getUsername()
                        .equals(authenticationService.getCurrentUser().getUsername()))
                .toList();
    }

    public ImportHistoryItemDTO importDragonsFromFile(MultipartFile file, String username) throws Exception {
        ImportHistoryItem historyItem = new ImportHistoryItem();

        try {
            historyItem.setOwner(userRepository.findById(username).get());
        } catch (Exception e) {
            // TODO
        }

        try {
            List<DragonDTO> dragons = objectMapper.readValue(
                    file.getInputStream(), new TypeReference<>() {
                    });

            String fileUrl = commit(dragons, file, username);

            historyItem.setFileUrl(fileUrl);
            historyItem.setStatus(ImportStatus.COMPLETED);
            historyItem.setNumberOfAddedObjects(dragons.size());
        } catch (Exception e) {
            historyItem.setStatus(ImportStatus.FAILED);
            historyItem.setNumberOfAddedObjects(null);

            throw e;
        }

        return dtoMapper.mapTo(importHistoryRepository.save(historyItem));
    }

    public String commit(List<DragonDTO> dragons, MultipartFile file, String username) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("importTransaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        TransactionStatus dbTransaction = transactionManager.getTransaction(def);
        String fileName = username + "_" + System.currentTimeMillis()
                + ".json";
        boolean isDbReady = true;
        boolean isMinioReady = true;

        try {
            // Phase 1
            isDbReady = checkDatabaseReadiness();
            isMinioReady = checkMinioReadiness();

            if (!isDbReady || !isMinioReady) {
                throw new IllegalStateException("One or more resources are not ready to commit the transaction.");
            }

            // Phase 2
            dragonService.saveImportedDragonsList(dragons);

            InputStream fileInputStream = file.getInputStream();
            long fileSize = file.getSize();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(getImportsMinioBucketName())
                            .object(fileName)
                            .stream(fileInputStream, fileSize, -1)
                            .build());

            transactionManager.commit(dbTransaction);
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(io.minio.http.Method.GET)
                            .bucket(getImportsMinioBucketName())
                            .object(fileName)
                            .build());
        } catch (Exception e) {
            transactionManager.rollback(dbTransaction);
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(getImportsMinioBucketName())
                            .object(fileName)
                            .build());

            String guilty = !isDbReady ? "DB" : "S3";

            throw new TransactionResourceIsNotReady(guilty);
        }
    }

    private boolean checkDatabaseReadiness() {
        try {
            return dragonService.findAllDragons() != null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkMinioReadiness() {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(getImportsMinioBucketName())
                            .object("test-readiness")
                            .build());
            return true;
        } catch (Exception e) {
            log.error("MinIO readiness check failed", e);
            return false;
        }
    }
}
