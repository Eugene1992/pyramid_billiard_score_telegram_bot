package com.yede0517.edu.telegrambeerbetbot.engine.actions.frame;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Frame;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionLabels;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartFrameAction extends AbstractGameAction implements Action {

    public static final String BEGIN_FRAME_LABEL = "Партия началась!";

    private final GameService gameService;

    @Override
    public void apply(Long chatId, Update update) {
        Game game = gameService.getActiveGame(chatId);
        Frame newFrame = new Frame(game.getFirstPlayer(), game.getSecondPlayer());
        game.addFrame(newFrame);
        game.getFirstPlayer().setActive(true);
        game.getSecondPlayer().setActive(false);

        gameService.update(game);
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        Game activeGame = gameService.getActiveGame(chatId);

        return getActiveFrameKeyboard(activeGame, BEGIN_FRAME_LABEL);
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionLabels.START_FRAME_LABEL);
    }
}
