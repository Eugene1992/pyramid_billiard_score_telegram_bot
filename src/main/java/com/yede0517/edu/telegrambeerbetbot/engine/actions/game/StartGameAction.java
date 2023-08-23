package com.yede0517.edu.telegrambeerbetbot.engine.actions.game;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.service.KeyboardService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.GameStatus;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionLabels;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartGameAction extends AbstractGameAction implements Action {

    private final GameService gameService;
    private final KeyboardService keyboardService;

    @Override
    public void apply(Long chatId, Update update) {
        Game queuedGame = gameService.getQueuedGame(chatId);
        queuedGame.setStart(LocalDateTime.now());
        queuedGame.setStatus(GameStatus.IN_PROGRESS);

        gameService.update(queuedGame);
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        Game activeGame = gameService.getActiveGame(chatId);

        return keyboardService.getGameMenuKeyboard(activeGame, "Выберите:");
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionLabels.START_GAME_LABEL);
    }
}
