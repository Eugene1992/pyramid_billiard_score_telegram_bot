package com.yede0517.edu.telegrambeerbetbot.data.entity;

import java.util.Arrays;

import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import lombok.Getter;

@Getter
public enum SpecialBallType {
    RACK_SHOT("С разбоя"),
    ABRIKOL_SHOT("Абриколь"),
    DOUBLE_SHOT("Дуплет"),
    LONG_SHOT("Дальний"),
    FRENCH_SHOT("Француз"),
    SYSTEM_SHOT("Система");

    private String name;

    SpecialBallType(String name) {
        this.name = name;
    }

    public static SpecialBallType of(Integer ordinal) {
        return Arrays.stream(values())
                .filter(type -> type.ordinal() == ordinal)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Failed to find special ball type by system type"));
    }
}