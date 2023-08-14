package com.yede0517.edu.telegrambeerbetbot.engine.actions.frame;

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
public class ConfirmEndFrameAction extends AbstractGameAction implements Action {

    public static final String END_FRAME_CONFIRMATION_LABEL = "Вы действительно хотите завершить эту партию?";

    @Override
    public void apply(Long chatId, Update update) {
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        return new ReplyKeyboardMarkupBuilder(END_FRAME_CONFIRMATION_LABEL)
                .row(ActionLabels.END_FRAME_LABEL, ActionLabels.RESUME_FRAME_LABEL)
                .build();
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionIcons.STOP_ACTION_ICON) && command.contains(ActionIcons.FRAME_ICON);

    }
}
