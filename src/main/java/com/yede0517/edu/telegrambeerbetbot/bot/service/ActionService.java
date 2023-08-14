package com.yede0517.edu.telegrambeerbetbot.bot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.commands.ICommand;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;

@Service
public class ActionService {

    @Autowired
    private List<Action> allActions;

    public Action applyAction(Long chatId, Update update) {
        String message = update.getMessage().getText();
        Action action = findAction(message);
        action.apply(chatId, update);

        return action;
    }

    private Action findAction(String actionName) {
        return allActions.stream()
                .filter(action -> action.isAppropriate(actionName))
                .findFirst()
                .orElse(null);
    }

    public boolean isAction(Update update) {
        String message = update.getMessage().getText();

        return findAction(message) != null;
    }
}
