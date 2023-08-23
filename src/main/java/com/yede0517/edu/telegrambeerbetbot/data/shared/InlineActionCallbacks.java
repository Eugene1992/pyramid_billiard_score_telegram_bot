package com.yede0517.edu.telegrambeerbetbot.data.shared;

public interface InlineActionCallbacks {

    String SET_BALLS_STREAK_ACTION_PREFIX = "#setBallsStreak";
    String SET_BALLS_STREAK_CALLBACK_PATTERN = "#setBallsStreak(game:%s_frame:%s_point:%s_size:%s)";

    String CHOOSE_BALLS_STREAK_ACTION_PREFIX = "#chooseBallsStreak";
    String CHOOSE_BALLS_STREAK_CALLBACK_PATTERN = "#chooseBallsStreak(game:%s_frame:%s_point:%s)";

    String SET_SPECIAL_BALL_ACTION_PREFIX = "#setSpecialBallType";
    String SET_SPECIAL_BALL_CALLBACK_PATTERN = "#setSpecialBallType(game:%s_frame:%s_point:%s_type:%s)";

    String CLEAR_SPECIAL_BALL_ACTION_PREFIX = "#clearSpecialBallType";
    String CLEAR_SPECIAL_BALL_CALLBACK_PATTERN = "#clearSpecialBallType(game:%s_frame:%s_point:%s)";
}
