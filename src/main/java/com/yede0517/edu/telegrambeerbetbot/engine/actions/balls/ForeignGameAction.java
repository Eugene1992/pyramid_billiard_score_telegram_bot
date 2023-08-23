package com.yede0517.edu.telegrambeerbetbot.engine.actions.balls;

import static com.yede0517.edu.telegrambeerbetbot.data.entity.SpecialBallType.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.BallType;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Frame;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Point;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ForeignGameAction extends AbstractGameAction implements Action {

    private final GameService gameService;


    @Override
    public void apply(Long chatId, Update update) {
        super.countBall(chatId, BallType.FOREIGN);
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        return keyboardService.getActiveFrameKeyboardAfterBallScore(chatId, BallType.FOREIGN);
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionIcons.FOREIGN_BALL_ICON);
    }

    @Override
    public List<SendMessage> getPostResponses(Long chatId, Update update) {
        return singletonList(keyboardService.getSpecialBallKeyboard(chatId));
    }
}
