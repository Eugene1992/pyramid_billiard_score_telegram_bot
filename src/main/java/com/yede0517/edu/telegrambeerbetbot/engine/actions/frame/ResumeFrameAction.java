package com.yede0517.edu.telegrambeerbetbot.engine.actions.frame;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ResumeFrameAction extends AbstractGameAction implements Action {

    public static final String CONTINUE_GAME_LABEL = "Продолжаем игру:";

    private final GameService gameService;

    @Override
    public void apply(Long chatId, Update update) {
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        Game activeGame = gameService.getActiveGame(chatId);

        return getActiveFrameKeyboard(activeGame, CONTINUE_GAME_LABEL);
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionIcons.RESUME_ACTION_ICON) && command.contains(ActionIcons.FRAME_ICON);
    }
}
