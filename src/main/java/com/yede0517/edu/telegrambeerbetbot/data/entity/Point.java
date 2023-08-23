package com.yede0517.edu.telegrambeerbetbot.data.entity;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    private Integer number;
    private UUID scorerId;
    private BallType ballType;
    private SpecialBallType specialBallType;
    private boolean isStreak;

    public Point(Integer number, UUID scorerId, BallType ballType) {
        this.number = number;
        this.scorerId = scorerId;
        this.ballType = ballType;
    }
}
