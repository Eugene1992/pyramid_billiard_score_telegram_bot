package com.yede0517.edu.telegrambeerbetbot.data.entity;

import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import lombok.Getter;

@Getter
public enum BallType {
    OWN(ActionIcons.OWN_BALL_ICON, "свой"),
    FOREIGN(ActionIcons.FOREIGN_BALL_ICON, "чужой"),
    PENALTY(ActionIcons.PENALTY_BALL_ICON, "штраф"),
    PRESENT(ActionIcons.PRESENT_BALL_ICON, "подстава"),
    DUMMY(ActionIcons.DUMMY_BALL_ICON, "дурак");

    private String icon;
    private String name;

    BallType(String icon, String name) {
        this.icon = icon;
        this.name = name;
    }
}