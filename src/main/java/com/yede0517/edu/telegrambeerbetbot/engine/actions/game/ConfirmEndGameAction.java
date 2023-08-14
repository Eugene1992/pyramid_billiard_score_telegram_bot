package com.yede0517.edu.telegrambeerbetbot.engine.actions.game;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.utils.ReplyKeyboardMarkupBuilder;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionLabels;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConfirmEndGameAction extends AbstractGameAction implements Action {

    public static final String END_GAME_CONFIRMATION_LABEL = "Вы действительно хотите завершить эту игру?";

    @Override
    public void apply(Long chatId, Update update) {
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        return new ReplyKeyboardMarkupBuilder(END_GAME_CONFIRMATION_LABEL)
                .row(ActionLabels.END_GAME_LABEL, ActionLabels.RESUME_GAME_LABEL)
                .build();
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionIcons.STOP_ACTION_ICON) && command.contains(ActionIcons.GAME_ICON);
    }
}
