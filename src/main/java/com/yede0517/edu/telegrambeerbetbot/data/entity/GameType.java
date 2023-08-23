package com.yede0517.edu.telegrambeerbetbot.data.entity;

import static java.util.Arrays.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public enum GameType {
    FREE_PYRAMID("Свободная пирамида"),
    COMBINE_PYRAMID("Комбинированная пирамида");

    private String name;

    GameType(String name) {
        this.name = name;
    }

    public static GameType of(String name) {
        return stream(values())
                .filter(type -> type.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Failed to find game with the specified type"));
    }

    public static List<String> getTypeNames() {
        return stream(values())
                .map(GameType::getName)
                .collect(Collectors.toList());
    }
}
