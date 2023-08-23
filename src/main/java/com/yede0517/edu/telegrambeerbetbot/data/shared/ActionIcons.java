package com.yede0517.edu.telegrambeerbetbot.data.shared;

public interface ActionIcons {
    String GAME_ICON = "\uD83C\uDFAE";
    String FRAME_ICON = "\uD83C\uDFB1";

    String STOP_ACTION_ICON = "⏹";
    String END_ACTION_ICON = "❌";
    String RESUME_ACTION_ICON = "◀️";
    String CONTINUE_ACTION_ICON = "▶️";

    String STOP_GAME_ICON = STOP_ACTION_ICON + GAME_ICON;
    String END_GAME_ICON = END_ACTION_ICON + GAME_ICON;

    String STOP_FRAME_ICON = STOP_ACTION_ICON + FRAME_ICON;
    String END_FRAME_ICON = END_ACTION_ICON + FRAME_ICON;

    String CALENDAR_ICON = "\uD83D\uDCC5";
    String SCORE_ICON = "\uD83D\uDD22";
    String GAME_HISTORY_ICON = "\uD83D\uDD39";
    String WINNER_PLAYER_ICON = "\uD83C\uDFC6";
    String ACTIVE_PLAYER_ICON = "⭐️";
    String NOT_ACTIVE_PLAYER_ICON = "▪️";
    String PLAYER_ICON = "\uD83E\uDDD4\u200D♂️";
    String STATISTICS_ICON = "\uD83D\uDCCA";

    String OWN_BALL_ICON = "\uD83D\uDFE2";
    String FOREIGN_BALL_ICON = "\uD83D\uDD35";
    String PENALTY_BALL_ICON = "\uD83D\uDD34";
    String PRESENT_BALL_ICON = "⚪️";
    String DUMMY_BALL_ICON = "\uD83D\uDFE4";

    String RETURN_LEFT_ICON = "↩️";
    String RETURN_RIGHT_ICON = "↪️";
    String NOTES_ICON = "\uD83D\uDCDD";
    String CLOCK_ICON = "\uD83D\uDD58";
    String DOUBLE_EXPLANATION_MARK_ICON = "‼️ ";

    String RESUME_FRAME_ICON = "\uD83D\uDD19";
    String RESUME_GAME_ICON = "\uD83D\uDD1C";
    String GAME_TYPE_ICON = "\uD83C\uDFAF ";

    String STREAK_BALL_ICON = "\uD83D\uDFE0";
}
