package com.example.prac.service.data;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.prac.dto.data.DragonDTO;
import com.example.prac.exceptions.NotEnoughRightsException;
import com.example.prac.mappers.Mapper;
import com.example.prac.model.auth.Role;
import com.example.prac.model.auth.User;
import com.example.prac.model.data.Dragon;
import com.example.prac.repository.data.DragonRepository;

import jakarta.persistence.ParameterMode;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DragonService {
    private final DragonRepository dragonRepository;
    private final Mapper<Dragon, DragonDTO> dragonMapper;
    private final SessionFactory sessionFactory;

    @Autowired
    public DragonService(SessionFactory sessionFactory, Mapper<Dragon, DragonDTO> dragonMapper,
            DragonRepository dragonRepository) {
        this.sessionFactory = sessionFactory;
        this.dragonRepository = dragonRepository;
        this.dragonMapper = dragonMapper;
    }

    public Integer getTotalAge() {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery("SELECT * FROM total_age()", Integer.class).getSingleResult();
        }
    }

    public Optional<DragonDTO> getDragonWithGigachadKiller() {
        try (Session session = sessionFactory.openSession()) {
            Long dragonID = session.createNativeQuery("SELECT * FROM get_dragon_id_with_gigachad_killer()", Long.class)
                    .getSingleResult();

            return findById(dragonID);
        }
    }

    public Optional<DragonDTO> getDragonWithTheDeepestCave() {
        try (Session session = sessionFactory.openSession()) {
            Long dragonID = session.createNativeQuery("SELECT * FROM get_dragon_with_the_deepest_cave()", Long.class)
                    .getSingleResult();

            return findById(dragonID);
        }
    }

    public List<DragonDTO> findDragonsByNameSubstring(String nameSubstring) {
        try (Session session = sessionFactory.openSession()) {
            List<Long> dragonIDs = session
                    .createNativeQuery("SELECT * FROM get_dragon_ids_by_name_substring(:nameSubstring)", Long.class)
                    .setParameter("nameSubstring", nameSubstring)
                    .list();

            return dragonIDs.stream().map(id -> findById(id).get()).toList();
        }
    }

    public void createKillersGang() {
        try (ProcedureCall call = sessionFactory.openSession().createStoredProcedureCall("create_killers_gang")) {
            var gangID = generateRandomDigitalID();
            call
                    .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
                    .setParameter(1, generatePassportId(gangID))
                    .setParameter(2, generatePassportId(gangID))
                    .setParameter(3, generatePassportId(gangID))
                    .execute();
        }
    }

    public DragonDTO save(DragonDTO dragonDTO) {
        Dragon dragon = dragonMapper.mapFrom(dragonDTO);
        dragon.setCreationDate(new Date());
        dragon.setDragonOwner(getCurrentUser());

        return dragonMapper.mapTo(dragonRepository.save(dragon));
    }

    public List<DragonDTO> findAllDragons() {
        return StreamSupport.stream(dragonRepository.findAll().spliterator(), false)
                .map(dragonMapper::mapTo).toList();
    }

    public Optional<DragonDTO> findById(Long dragonId) {
        Optional<Dragon> optionalDragon = dragonRepository.findById(dragonId);
        return optionalDragon.map(dragonMapper::mapTo);
    }

    public boolean isExists(Long dragonId) {
        return dragonRepository.existsById(dragonId);
    }

    public DragonDTO partialUpdate(Long dragonId, DragonDTO dragonDTO) {
        dragonDTO.setId(dragonId);
        return dragonRepository.findById(dragonId).map(existingDragon -> {
            if (!checkUserOwnsDragon(existingDragon)) {
                throw new NotEnoughRightsException("User hasn't enough right to update this object");
            }

            Dragon dragonUpdate = dragonMapper.mapFrom(dragonDTO);

            Optional.ofNullable(dragonUpdate.getName()).ifPresent(existingDragon::setName);
            Optional.ofNullable(dragonUpdate.getCoordinates()).ifPresent(existingDragon::setCoordinates);
            Optional.ofNullable(dragonUpdate.getCave()).ifPresent(existingDragon::setCave);
            Optional.ofNullable(dragonUpdate.getKiller()).ifPresent(existingDragon::setKiller);
            Optional.ofNullable(dragonUpdate.getAge()).ifPresent(existingDragon::setAge);
            Optional.ofNullable(dragonUpdate.getColor()).ifPresent(existingDragon::setColor);
            Optional.ofNullable(dragonUpdate.getType()).ifPresent(existingDragon::setType);
            Optional.ofNullable(dragonUpdate.getCharacter()).ifPresent(existingDragon::setCharacter);
            Optional.ofNullable(dragonUpdate.getHead()).ifPresent(existingDragon::setHead);

            return dragonMapper.mapTo(dragonRepository.save(existingDragon));
        }).orElseThrow(() -> new RuntimeException("Dragon doesn't exist"));
    }

    public void delete(Long dragonId) {
        dragonRepository.findById(dragonId).ifPresentOrElse(dragon -> {
            if ((getCurrentUser().getRole() == Role.ADMIN && dragon.getCanBeEditedByAdmin()) ||
                    checkUserOwnsDragon(dragon)) {
                dragonRepository.deleteById(dragonId);
            } else {
                throw new NotEnoughRightsException("User hasn't enough right to delete this object");
            }
        }, () -> new RuntimeException("Dragon doesn't exist"));
    }

    private boolean checkUserOwnsDragon(Dragon dragon) {
        return getCurrentUser().getId().equals(dragon.getDragonOwner().getId());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private String generatePassportId(int gangId) {
        return String.format("GANG-%d-%d", gangId, generateRandomDigitalID());
    }

    private int generateRandomDigitalID() {
        return (int) (Math.random() * 10000);
    }
}