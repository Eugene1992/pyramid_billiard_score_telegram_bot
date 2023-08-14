package com.yede0517.edu.telegrambeerbetbot.data.entity;

import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import lombok.Getter;

@Getter
public enum BallType {
    OWN(ActionIcons.OWN_BALL_ICON),
    FOREIGN(ActionIcons.FOREIGN_BALL_ICON),
    PENALTY(ActionIcons.PENALTY_BALL_ICON),
    PRESENT(ActionIcons.PRESENT_BALL_ICON),
    DUMMY(ActionIcons.DUMMY_BALL_ICON);

    private String icon;

    BallType(String icon) {
        this.icon = icon;
    }
}