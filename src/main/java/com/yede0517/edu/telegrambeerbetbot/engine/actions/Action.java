package com.yede0517.edu.telegrambeerbetbot.engine.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Action {

    void apply(Long chatId, Update update);

    SendMessage getResponse(Long chatId, Update update);

    boolean isAppropriate(String command);
}
