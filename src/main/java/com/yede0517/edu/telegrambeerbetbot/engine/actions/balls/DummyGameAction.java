package com.yede0517.edu.telegrambeerbetbot.engine.actions.balls;

import static java.util.Collections.singletonList;

import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.BallType;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Player;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DummyGameAction extends AbstractGameAction implements Action {

    @Override
    public void apply(Long chatId, Update update) {
        super.countBall(chatId, BallType.DUMMY);
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        return keyboardService.getActiveFrameKeyboardAfterBallScore(chatId, BallType.DUMMY);
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionIcons.DUMMY_BALL_ICON);
    }
    @Override
    public List<SendMessage> getPostResponses(Long chatId, Update update) {
        return singletonList(keyboardService.getSpecialBallKeyboard(chatId));
    }
}
