package com.yede0517.edu.telegrambeerbetbot.engine.actions.player;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.data.entity.Frame;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReturnFirstPlayerGameAction extends AbstractGameAction implements Action {

    @Override
    public void apply(Long chatId, Update update) {
        Game activeGame = gameService.getActiveGame(chatId);
        SendMessage response = new SendMessage();
        response.setText("Выберите действие:");

        Frame activeFrame = activeGame.getActiveFrame();
        activeFrame.removeFirstPlayerPoint();
        gameService.update(activeGame);
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        return super.getActiveFrameKeyboard(chatId);
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionIcons.RETURN_LEFT_ICON);
    }
}
