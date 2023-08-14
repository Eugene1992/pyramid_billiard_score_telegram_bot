package com.yede0517.edu.telegrambeerbetbot.bot;

import static java.util.Objects.isNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yede0517.edu.telegrambeerbetbot.bot.service.ActionService;
import com.yede0517.edu.telegrambeerbetbot.bot.service.CommandService;
import com.yede0517.edu.telegrambeerbetbot.bot.service.FreemakerService;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BotRunner extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Autowired
    private CommandService commandService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private FreemakerService freemakerService;

    @Override
    public void onUpdateReceived(Update update) {
        Message incomeMessage = update.getMessage();
        Long chatId = incomeMessage.getChatId();
        String incomeMessageText = incomeMessage.getText();

        boolean isCommand = commandService.isCommand(update);
        SendMessage message = null;

        if (isCommand) {
            message = commandService.applyCommand(update);
        }

        boolean isAction = actionService.isAction(update);

        if (isAction) {
            Action action = actionService.applyAction(chatId, update);
            message = action.getResponse(chatId, update);
        }

        if (isNull(message)) {
            message = new SendMessage(String.valueOf(chatId), incomeMessageText);
        }

        try {
            if (incomeMessageText.equals("photo")) {
                message.disableWebPagePreview();
                message.setChatId(chatId);
                message.setParseMode(ParseMode.MARKDOWNV2);
                execute(message);
            } else {
                message.setChatId(chatId);
                Message result = execute(message);
            }
            log.info("Command was successfully executed");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}