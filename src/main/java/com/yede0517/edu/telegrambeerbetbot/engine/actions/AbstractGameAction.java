package com.yede0517.edu.telegrambeerbetbot.engine.actions;

import static java.util.Objects.nonNull;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.utils.ReplyKeyboardMarkupBuilder;
import com.yede0517.edu.telegrambeerbetbot.data.entity.BallType;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Frame;
import com.yede0517.edu.telegrambeerbetbot.data.entity.FrameStatus;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Player;
import com.yede0517.edu.telegrambeerbetbot.data.entity.PlayerScore;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionLabels;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class AbstractGameAction {

    @Autowired
    protected GameService gameService;

    public void countBall(Long chatId, BallType ballType) {
        Game game = gameService.getActiveGame(chatId);

        Frame activeFrame = game.getActiveFrame();
        UUID scorerId = game.getActivePlayer().getId();
        activeFrame.addPoint(scorerId, ballType);

        if (activeFrame.isEnd()) {
            activeFrame.setEnd(LocalDateTime.now());
            activeFrame.setStatus(FrameStatus.COMPLETED);
            game.recalculatePlayerScores();
        }

        gameService.update(game);
    }

    public SendMessage getGameMenuKeyboard(Game game, String text) {
        game.recalculatePlayerScores();

        PlayerScore firstPlayerScore = game.getFirstPlayerScore();
        PlayerScore secondPlayerScore = game.getSecondPlayerScore();

        String firstPlayerFullName = game.getFirstPlayer().getFullName();
        String secondPlayerFullName = game.getSecondPlayer().getFullName();

        return new ReplyKeyboardMarkupBuilder(text)
                .row(
                        firstPlayerFullName,
                        firstPlayerScore.getScore() + " - " + secondPlayerScore.getScore(),
                        secondPlayerFullName
                )
                .row(ActionLabels.START_FRAME_LABEL,
                        ActionIcons.STATISTICS_ICON + "Статистика",
                        ActionIcons.STOP_ACTION_ICON + ActionIcons.GAME_ICON + "Завершить игру")
                .build();
    }

    public SendMessage getActiveFrameKeyboard(Long chatId) {
        return getActiveFrameKeyboard(chatId, "Выберите действие:");
    }

    public SendMessage getActiveFrameKeyboard(Long chatId, String text) {
        Game activeGame = gameService.getActiveGame(chatId);

        if (nonNull(activeGame.getActiveFrame())) {
            return getActiveFrameKeyboard(activeGame, text);
        } else {
            return getGameMenuKeyboard(activeGame, "Выберите действие:");
        }
    }

    public SendMessage getActiveFrameKeyboard(Game activeGame, String text) {
        Frame activeFrame = activeGame.getActiveFrame();

        if (nonNull(activeFrame)) {
            Player firstPlayer = activeGame.getFirstPlayer();
            Player secondPlayer = activeGame.getSecondPlayer();
            String firstPlayerFullName = firstPlayer.getFullName();
            String firstPlayerName = firstPlayer.isActive() ? ActionIcons.ACTIVE_PLAYER_ICON + firstPlayerFullName
                    : ActionIcons.NOT_ACTIVE_PLAYER_ICON + firstPlayerFullName;
            String secondPlayerFullName = secondPlayer.getFullName();
            String secondPlayerName = secondPlayer.isActive() ? ActionIcons.ACTIVE_PLAYER_ICON + secondPlayerFullName
                    : ActionIcons.NOT_ACTIVE_PLAYER_ICON + secondPlayerFullName;
            PlayerScore firstPlayerScore = activeFrame.getFirstPlayerScore();
            PlayerScore secondPlayerScore = activeFrame.getSecondPlayerScore();

            return new ReplyKeyboardMarkupBuilder(text)
                    .row(
                            firstPlayerName,
                            firstPlayerScore.getScore() + " - " + secondPlayerScore.getScore(),
                            secondPlayerName)
                    .row(
                            ActionIcons.OWN_BALL_ICON + " Свой",
                            ActionIcons.FOREIGN_BALL_ICON + " Чужой",
                            ActionIcons.PENALTY_BALL_ICON + " Штраф",
                            ActionIcons.PRESENT_BALL_ICON + " Подстава",
                            ActionIcons.DUMMY_BALL_ICON + " Дурак"
                    )
                    .row(
                            ActionIcons.RETURN_LEFT_ICON + "Отменить",
                            ActionIcons.STOP_ACTION_ICON + ActionIcons.FRAME_ICON + " Завершить партию",
                            ActionIcons.RETURN_RIGHT_ICON + "Отменить")
                    .build();
        } else {
            return getGameMenuKeyboard(activeGame, "Выберите действие:");
        }
    }

    public SendMessage getStartGameKeyboard(Game game, String text) {
        String firstPlayerFullName = game.getFirstPlayer().getFullName();
        String secondPlayerFullName = game.getSecondPlayer().getFullName();

        return new ReplyKeyboardMarkupBuilder(text)
                .row(
                        ActionIcons.PLAYER_ICON + firstPlayerFullName,
                        ActionIcons.PLAYER_ICON + secondPlayerFullName
                )
                .row(ActionLabels.START_GAME_LABEL)
                .build();
    }
}
