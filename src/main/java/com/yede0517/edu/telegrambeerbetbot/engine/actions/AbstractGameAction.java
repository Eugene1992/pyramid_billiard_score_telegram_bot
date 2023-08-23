package com.yede0517.edu.telegrambeerbetbot.engine.actions;

import static java.util.Collections.emptyList;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.service.KeyboardService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.BallType;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Frame;
import com.yede0517.edu.telegrambeerbetbot.data.entity.FrameStatus;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public abstract class AbstractGameAction implements Action {

    @Autowired
    protected GameService gameService;

    @Autowired
    protected KeyboardService keyboardService;

    @Override
    public List<SendMessage> getPostResponses(Long chatId, Update update) {
        return emptyList();
    }

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

    public Map<String, String> getCallbackProperties(String command, String prefix) {
        return Arrays.stream(command.replaceAll(prefix, "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .split("_"))
                .map(s -> s.split(":"))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));
    }
}
