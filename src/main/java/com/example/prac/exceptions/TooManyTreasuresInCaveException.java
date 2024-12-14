package com.example.prac.exceptions;

import com.example.prac.service.data.DragonService;

public class TooManyTreasuresInCaveException extends RuntimeException {
    public TooManyTreasuresInCaveException(String dragonName, long treasures) {
        super(String.format("Too many treasures in dragon %s's cave: %d. Max is %d.", dragonName, treasures,
                DragonService.MAX_DRAGON_CAVE_TREASURES));
    }
}
