package com.yede0517.edu.telegrambeerbetbot.engine.actions.balls;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.data.entity.BallType;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PresentGameAction extends AbstractGameAction implements Action {

    @Override
    public void apply(Long chatId, Update update) {
        super.countBall(chatId, BallType.PRESENT);
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        return super.getActiveFrameKeyboard(chatId);
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionIcons.PRESENT_BALL_ICON);
    }
}
