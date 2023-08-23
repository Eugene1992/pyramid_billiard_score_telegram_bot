package com.yede0517.edu.telegrambeerbetbot.data.entity;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PointStreak {
    private UUID scorerId;
    private Integer fromPoint;
    private Integer toPoint;
}
