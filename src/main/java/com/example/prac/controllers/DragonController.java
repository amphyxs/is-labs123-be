package com.example.prac.controllers;

import com.example.prac.DTO.data.DragonDTO;
import com.example.prac.service.data.DragonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dragons")
@AllArgsConstructor
public class DragonController {

    private final DragonService dragonService;

    @PostMapping
    public ResponseEntity<DragonDTO> createDragon(@RequestBody DragonDTO dragonDTO) {
        DragonDTO savedDragon = dragonService.save(dragonDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDragon);
    }

    @GetMapping
    public ResponseEntity<List<DragonDTO>> getAllDragons() {
        List<DragonDTO> dragons = dragonService.findAllDragons();
        return ResponseEntity.ok(dragons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DragonDTO> getDragonById(@PathVariable Long id) {
        return dragonService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DragonDTO> updateDragonPartially(@PathVariable Long id, @RequestBody DragonDTO dragonDTO) {
        try {
            DragonDTO updatedDragon = dragonService.partialUpdate(id, dragonDTO);
            return ResponseEntity.ok(updatedDragon);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDragon(@PathVariable Long id) {
        if (dragonService.isExists(id)) {
            dragonService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}