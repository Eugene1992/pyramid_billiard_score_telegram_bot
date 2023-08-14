package com.yede0517.edu.telegrambeerbetbot.bot.service;

import com.yede0517.edu.telegrambeerbetbot.bot.commands.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class CommandService {

    @Autowired
    private ApplicationContext context;


    public SendMessage applyCommand(Update update) {
        String message = update.getMessage().getText();

        String commandName = getCommand(message);

        ICommand command = findCommand(commandName);

        return command.apply(update);
    }

    private ICommand findCommand(String commandName) {
        return this.context.getBean(commandName, ICommand.class);
    }

    private String getCommand(String message) {
        if (message.contains(" ")) {
            int end = message.indexOf(" ");
            message = message.substring(0, end);
        }

        return message.substring(1);
    }

    public boolean isCommand(Update update) {
        String message = update.getMessage().getText();

        return message.startsWith("/");
    }
}
