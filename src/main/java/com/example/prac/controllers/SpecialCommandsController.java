package com.example.prac.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.prac.DTO.data.DragonDTO;
import com.example.prac.service.data.DragonService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/special-commands")
@AllArgsConstructor
public class SpecialCommandsController {

    private final DragonService dragonService;
    
    @GetMapping("/sum-dragon-ages")
    public ResponseEntity<Integer> getTotalAge() {
        return ResponseEntity.ok(dragonService.getTotalAge());
    }

    @GetMapping("/dragon-with-gigachad-killer")
    public ResponseEntity<Optional<DragonDTO>> getDragonWithGigachadKiller() {
        return ResponseEntity.ok(dragonService.getDragonWithGigachadKiller());
    }

    @PostMapping("/find-dragons-by-name-substring")
    public ResponseEntity<List<DragonDTO>> findDragonsByNameSubstring(@RequestBody String nameSubstring) {
        return ResponseEntity.ok(dragonService.findDragonsByNameSubstring(nameSubstring));
    }

    @GetMapping("/dragon-with-the-deepest-cave")
    public ResponseEntity<Optional<DragonDTO>> getDragonWithTheDeepestCave() {
        return ResponseEntity.ok(dragonService.getDragonWithTheDeepestCave());
    }

    @PostMapping("/create-killers-gang")
    public ResponseEntity<Void> createKillersGang() {
        dragonService.createKillersGang();
        return ResponseEntity.ok().build();
    }
}

