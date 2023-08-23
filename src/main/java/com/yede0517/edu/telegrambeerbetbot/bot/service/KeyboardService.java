package com.yede0517.edu.telegrambeerbetbot.bot.service;

import static com.yede0517.edu.telegrambeerbetbot.data.entity.SpecialBallType.RACK_SHOT;
import static com.yede0517.edu.telegrambeerbetbot.data.shared.InlineActionCallbacks.CHOOSE_BALLS_STREAK_CALLBACK_PATTERN;
import static com.yede0517.edu.telegrambeerbetbot.data.shared.InlineActionCallbacks.CLEAR_SPECIAL_BALL_CALLBACK_PATTERN;
import static com.yede0517.edu.telegrambeerbetbot.data.shared.InlineActionCallbacks.SET_BALLS_STREAK_CALLBACK_PATTERN;
import static com.yede0517.edu.telegrambeerbetbot.data.shared.InlineActionCallbacks.SET_SPECIAL_BALL_CALLBACK_PATTERN;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.yede0517.edu.telegrambeerbetbot.bot.utils.ReplyKeyboardMarkupBuilder;
import com.yede0517.edu.telegrambeerbetbot.data.entity.BallType;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Frame;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Player;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Point;
import com.yede0517.edu.telegrambeerbetbot.data.entity.SpecialBallType;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionLabels;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    private final GameService gameService;

    public SendMessage getStartGameKeyboard(Game game, String text) {
        return new ReplyKeyboardMarkupBuilder(text)
                .row(getPlayersGameScoreRow(game, false, false))
                .row(ActionLabels.START_GAME_LABEL)
                .build();
    }

    public SendMessage getActiveFrameKeyboardAfterBallScore(Long chatId, BallType ballType) {
        Game activeGame = gameService.getActiveGame(chatId);
        Player activePlayer = activeGame.getActivePlayer();

        String message = format("%s забивает %s %s", activePlayer.getFirstName(), ballType.getName(), ballType.getIcon());

        return getActiveFrameKeyboard(chatId, message);
    }

    public SendMessage getActiveFrameKeyboard(Long chatId) {
        return getActiveFrameKeyboard(chatId, "Выберите действие:");
    }

    public SendMessage getActiveFrameKeyboard(Long chatId, String text) {
        Game activeGame = gameService.getActiveGame(chatId);

        boolean isFrameActive = nonNull(activeGame.getActiveFrame());
        if (isFrameActive) {
            return getActiveFrameKeyboard(activeGame, text);
        } else {
            return getGameMenuKeyboard(activeGame, text);
        }
    }

    public SendMessage getActiveFrameKeyboard(Game activeGame, String text) {
        Frame activeFrame = activeGame.getActiveFrame();

        if (nonNull(activeFrame)) {
            return new ReplyKeyboardMarkupBuilder(text)
                    .row(getPlayersFrameScoreRow(activeGame, activeFrame))
                    .row(
                            ActionLabels.OWN_BALL_LABEL,
                            ActionLabels.FOREIGN_BALL_LABEL,
                            ActionLabels.PENALTY_BALL_LABEL,
                            ActionLabels.PRESENT_BALL_LABEL,
                            ActionLabels.DUMMY_BALL_LABEL
                    )
                    .row(
                            ActionLabels.RETURN_LEFT_PLAYER_LABEL,
                            ActionLabels.STOP_FRAME_LABEL,
                            ActionLabels.RETURN_RIGHT_PLAYER_LABEL
                    )
                    .build();
        } else {
            return getGameMenuKeyboard(activeGame, "Выберите действие:");
        }
    }

    public SendMessage getGameMenuKeyboard(Game game, String text) {
        game.recalculatePlayerScores();

        return new ReplyKeyboardMarkupBuilder(text)
                .row(getPlayersGameScoreRow(game, true, false))
                .row(ActionLabels.START_FRAME_LABEL, ActionLabels.STATISTICS_LABEL, ActionLabels.STOP_GAME_LABEL)
                .build();
    }

    public SendMessage getBallStreakKeyboard(Long chatId, int gameNumber, int frameNumber, int pointNumber, int streakSize) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите размер серии:");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> buttons = new ArrayList<>();

        for (int num = 2; num <= streakSize; num++) {
            InlineKeyboardButton button = new InlineKeyboardButton("" + num);
            button.setCallbackData(format(SET_BALLS_STREAK_CALLBACK_PATTERN, gameNumber, frameNumber, pointNumber, num));

            buttons.add(button);
        }

        inlineKeyboardMarkup.setKeyboard(singletonList(
                buttons
        ));
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }

    public SendMessage getSpecialBallKeyboard(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Пометить как:");

        Game activeGame = gameService.getActiveGame(chatId);
        Frame activeFrame = activeGame.getLastFrame();
        Point lastPoint = activeFrame.getLastPoint();
        Integer frameNumber = activeFrame.getNumber();
        Integer pointNumber = lastPoint.getNumber();

        List<InlineKeyboardButton> buttons = getSpecialBallInlineButtons(activeGame, SET_SPECIAL_BALL_CALLBACK_PATTERN);

        /*boolean isFirstPoint = lastPoint.getNumber() == 1;

        if (isFirstPoint) {
            InlineKeyboardButton rackShot = new InlineKeyboardButton("С разбоя");
            rackShot.setCallbackData(format(SET_SPECIAL_BALL_CALLBACK_PATTERN, chatId, frameNumber, pointNumber, RACK_SHOT.ordinal()));
            buttons.add(rackShot);
        }

        InlineKeyboardButton abrikolShot = new InlineKeyboardButton("Абриколь");
        abrikolShot.setCallbackData(format(SET_SPECIAL_BALL_CALLBACK_PATTERN, chatId, frameNumber, pointNumber, ABRIKOL_SHOT.ordinal()));
        buttons.add(abrikolShot);

        InlineKeyboardButton doubleShot = new InlineKeyboardButton("Дуплет");
        doubleShot.setCallbackData(format(SET_SPECIAL_BALL_CALLBACK_PATTERN, chatId, frameNumber, pointNumber, DOUBLE_SHOT.ordinal()));
        buttons.add(doubleShot);

        InlineKeyboardButton longShot = new InlineKeyboardButton("Дальний");
        longShot.setCallbackData(format(SET_SPECIAL_BALL_CALLBACK_PATTERN, chatId, frameNumber, pointNumber, LONG_SHOT.ordinal()));
        buttons.add(longShot);

        InlineKeyboardButton frenchShot = new InlineKeyboardButton("Француз");
        frenchShot.setCallbackData(format(SET_SPECIAL_BALL_CALLBACK_PATTERN, chatId, frameNumber, pointNumber, FRENCH_SHOT.ordinal()));
        buttons.add(frenchShot);

        InlineKeyboardButton systemShot = new InlineKeyboardButton("Система");
        systemShot.setCallbackData(format(SET_SPECIAL_BALL_CALLBACK_PATTERN, chatId, frameNumber, pointNumber, SYSTEM_SHOT.ordinal()));
        buttons.add(systemShot);*/

        InlineKeyboardButton streakShot = new InlineKeyboardButton("Серия");
        streakShot.setCallbackData(format(CHOOSE_BALLS_STREAK_CALLBACK_PATTERN, chatId, frameNumber, pointNumber));
        buttons.add(streakShot);

        InlineKeyboardButton clear = new InlineKeyboardButton("\uD83D\uDDD1");
        clear.setCallbackData(format(CLEAR_SPECIAL_BALL_CALLBACK_PATTERN, chatId, frameNumber, pointNumber));
        buttons.add(clear);

        int buttonsCount = buttons.size();
        List<InlineKeyboardButton> firstRow = buttons.subList(0, buttonsCount / 2);
        List<InlineKeyboardButton> secondRow = buttons.subList(buttonsCount / 2, buttonsCount);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.setKeyboard(asList(
                firstRow,
                secondRow
        ));
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }

    private List<InlineKeyboardButton> getSpecialBallInlineButtons(Game game, String callbackPattern) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        Long gameNumber = game.getGameNumber();
        Frame activeFrame = game.getLastFrame();
        Point lastPoint = activeFrame.getLastPoint();
        Integer frameNumber = activeFrame.getNumber();
        Integer pointNumber = lastPoint.getNumber();

        for (SpecialBallType specialBallType : SpecialBallType.values()) {
            String specialBallTypeName = specialBallType.getName();
            int specialBallTypeNumber = specialBallType.ordinal();

            if (RACK_SHOT.equals(specialBallType) && lastPoint.getNumber() != 1) {
                continue;
            }

            InlineKeyboardButton button = new InlineKeyboardButton(specialBallTypeName);
            button.setCallbackData(format(callbackPattern, gameNumber, frameNumber, pointNumber, specialBallTypeNumber));

            buttons.add(button);
        }

        return buttons;
    }

    private String[] getPlayersGameScoreRow(Game game, boolean setScore, boolean setActivePlayerIcon) {
        Integer firstPlayerScore = game.getFirstPlayerScore().getScore();
        Integer secondPlayerScore = game.getSecondPlayerScore().getScore();

        return getPlayersScoreRow(game, firstPlayerScore, secondPlayerScore, setScore, setActivePlayerIcon);
    }

    private String[] getPlayersFrameScoreRow(Game game, Frame frame) {
        Integer firstPlayerScore = frame.getFirstPlayerScore().getScore();
        Integer secondPlayerScore = frame.getSecondPlayerScore().getScore();

        return getPlayersScoreRow(game, firstPlayerScore, secondPlayerScore, true, true);
    }

    private String[] getPlayersScoreRow(Game game, Integer firstPlayerScore, Integer secondPlayerScore,
                                        boolean setScore, boolean setActivePlayerIcon) {
        String firstPlayerName = getPlayerName(game.getFirstPlayer(), setActivePlayerIcon);
        String secondPlayerName = getPlayerName(game.getSecondPlayer(), setActivePlayerIcon);

        if (setScore) {
            return new String[]{firstPlayerName, firstPlayerScore + " - " + secondPlayerScore, secondPlayerName};
        } else {
            return new String[]{firstPlayerName, secondPlayerName};
        }
    }


    private String getPlayerName(Player player, boolean setActivePlayerIcon) {
        String playerName = player.getFullName();

        if (setActivePlayerIcon) {
            String activePlayerIcon = ActionIcons.ACTIVE_PLAYER_ICON;
            String notActivePlayerIcon = ActionIcons.NOT_ACTIVE_PLAYER_ICON;

            playerName = player.isActive() ? activePlayerIcon + playerName : notActivePlayerIcon + playerName;
        }

        return playerName;
    }
}
