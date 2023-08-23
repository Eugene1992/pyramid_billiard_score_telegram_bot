package com.yede0517.edu.telegrambeerbetbot.engine.actions.game;

import static java.lang.String.format;
import static java.lang.String.valueOf;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.service.KeyboardService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Frame;
import com.yede0517.edu.telegrambeerbetbot.data.entity.FrameStatus;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionLabels;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EndFrameAction extends AbstractGameAction implements Action {

    private final GameService gameService;
    private final KeyboardService keyboardService;

    @Override
    public void apply(Long chatId, Update update) {
        Game queuedGame = gameService.getActiveGame(chatId);
        Frame activeFrame = queuedGame.getActiveFrame();
        activeFrame.setStatus(FrameStatus.TERMINATED);
        activeFrame.recalculatePlayerScores();

        gameService.update(queuedGame);
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        Game activeGame = gameService.getActiveGame(chatId);

        return keyboardService.getGameMenuKeyboard(activeGame, "Выберите действие:");
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionLabels.END_FRAME_LABEL);
    }
}
