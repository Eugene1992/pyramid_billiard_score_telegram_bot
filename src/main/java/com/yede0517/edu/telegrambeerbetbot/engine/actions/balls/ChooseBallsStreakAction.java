package com.yede0517.edu.telegrambeerbetbot.engine.actions.balls;

import static com.yede0517.edu.telegrambeerbetbot.data.shared.InlineActionCallbacks.CHOOSE_BALLS_STREAK_ACTION_PREFIX;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.google.common.collect.Lists;
import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Frame;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Point;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChooseBallsStreakAction extends AbstractGameAction implements Action {


    private final GameService gameService;

    @Override
    public void apply(Long chatId, Update update) {
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        String command = update.getMessage().getText();

        Map<String, String> properties = getCallbackProperties(command, CHOOSE_BALLS_STREAK_ACTION_PREFIX);

        int gameNumber = parseInt(properties.get("game"));
        int frameNumber = parseInt(properties.get("frame"));
        int pointNumber = parseInt(properties.get("point"));

        Game game = gameService.getActiveGame(chatId);
        Frame frame = game.getFrame(frameNumber);
        Point point = frame.getPoint(pointNumber);

        List<Point> streakPoints = countLastStreak(point.getScorerId(), frame.getPoints());
        int streakSize = streakPoints.size();

        return keyboardService.getBallStreakKeyboard(chatId, gameNumber, frameNumber, pointNumber, streakSize);
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.startsWith(CHOOSE_BALLS_STREAK_ACTION_PREFIX);
    }

    private List<Point> countLastStreak(UUID scorerId, List<Point> points) {
        List<Point> reversePointsList = Lists.reverse(points);
        List<Point> streakPoints = new ArrayList<>();

        for (Point point : reversePointsList) {
            if (point.getScorerId().equals(scorerId) && !point.isStreak()) {
                streakPoints.add(point);
            } else {
                return streakPoints;
            }
        }

        return streakPoints;
    }
}
