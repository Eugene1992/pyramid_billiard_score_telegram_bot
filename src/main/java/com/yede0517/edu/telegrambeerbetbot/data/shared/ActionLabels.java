package com.yede0517.edu.telegrambeerbetbot.data.shared;

public interface ActionLabels {

    // frame
    String START_FRAME_LABEL = ActionIcons.FRAME_ICON + "Начать партию";
    String STOP_FRAME_LABEL = ActionIcons.STOP_ACTION_ICON + ActionIcons.FRAME_ICON + " Завершить партию";
    String RESUME_FRAME_LABEL = ActionIcons.RESUME_ACTION_ICON + ActionIcons.FRAME_ICON + " Отменить";
    String END_FRAME_LABEL = ActionIcons.END_ACTION_ICON + ActionIcons.FRAME_ICON + " Завершить";

    // game
    String START_GAME_LABEL = ActionIcons.GAME_ICON + "Начать игру";
    String STOP_GAME_LABEL = ActionIcons.STOP_ACTION_ICON + ActionIcons.GAME_ICON + "Завершить игру";
    String RESUME_GAME_LABEL = ActionIcons.RESUME_ACTION_ICON +  ActionIcons.GAME_ICON + " Отменить";
    String END_GAME_LABEL = ActionIcons.END_ACTION_ICON + ActionIcons.GAME_ICON + " Завершить";

    // statistic
    String STATISTICS_LABEL = ActionIcons.STATISTICS_ICON + "Статистика";

    // balls
    String OWN_BALL_LABEL = ActionIcons.OWN_BALL_ICON + " Свой";
    String FOREIGN_BALL_LABEL = ActionIcons.FOREIGN_BALL_ICON + " Чужой";
    String PENALTY_BALL_LABEL = ActionIcons.PENALTY_BALL_ICON + " Штраф";
    String PRESENT_BALL_LABEL = ActionIcons.PRESENT_BALL_ICON + " Подстава";
    String DUMMY_BALL_LABEL = ActionIcons.DUMMY_BALL_ICON + " Подстава";

    String RETURN_LEFT_PLAYER_LABEL = ActionIcons.RETURN_LEFT_ICON + "Отменить";
    String RETURN_RIGHT_PLAYER_LABEL = ActionIcons.RETURN_RIGHT_ICON + "Отменить";
}
