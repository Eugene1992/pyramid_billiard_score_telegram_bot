package com.yede0517.edu.telegrambeerbetbot.engine.actions.balls;

import static com.yede0517.edu.telegrambeerbetbot.data.shared.InlineActionCallbacks.SET_BALLS_STREAK_ACTION_PREFIX;
import static java.lang.Integer.parseInt;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.google.common.collect.Lists;
import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.service.KeyboardService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Frame;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Point;
import com.yede0517.edu.telegrambeerbetbot.data.entity.PointStreak;
import com.yede0517.edu.telegrambeerbetbot.data.entity.SpecialBallType;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SetBallsStreakAction extends AbstractGameAction implements Action {

    private final GameService gameService;
    private final KeyboardService keyboardService;

    @Override
    public void apply(Long chatId, Update update) {
        String command = update.getMessage().getText();

        Map<String, String> properties = getCallbackProperties(command, SET_BALLS_STREAK_ACTION_PREFIX);

        int frameNumber = parseInt(properties.get("frame"));
        int pointNumber = parseInt(properties.get("point"));
        int size = parseInt(properties.get("size"));

        Game game = gameService.getActiveGame(chatId);
        Frame frame = game.getFrame(frameNumber);
        Point point = frame.getPoint(pointNumber);
        UUID scorerId = point.getScorerId();

        int from = point.getNumber() - size + 1;
        int to = point.getNumber();

        PointStreak existedStreak = frame.getStreak(to);
        if (nonNull(existedStreak)) {
            frame.removeStreak(existedStreak);
        }

        for (int i = from; i <= to; i++) {
            Point currPoint = frame.getPoint(i);
            currPoint.setStreak(true);
        }
        frame.addStreak(scorerId, from, to);


        gameService.update(game);
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        return keyboardService.getActiveFrameKeyboard(chatId, "Серия была успешно установлена!");
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.startsWith(SET_BALLS_STREAK_ACTION_PREFIX);
    }
}
