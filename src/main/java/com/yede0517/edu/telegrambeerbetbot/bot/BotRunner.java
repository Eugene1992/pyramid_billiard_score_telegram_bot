package com.yede0517.edu.telegrambeerbetbot.bot;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yede0517.edu.telegrambeerbetbot.bot.service.ActionService;
import com.yede0517.edu.telegrambeerbetbot.bot.service.CommandService;
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

    @Override
    public void onUpdateReceived(Update update) {
        Message incomeMessage = update.getMessage();
        if (nonNull(incomeMessage)) {
            processMessage(incomeMessage, update);
        }

        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (nonNull(callbackQuery)) {
            processCallbackQuery(callbackQuery, update);
        }
    }

    private void processCallbackQuery(CallbackQuery callbackQuery, Update update) {
        Message message = callbackQuery.getMessage();
        message.setText(callbackQuery.getData());
        update.setMessage(message);

        processMessage(message, update);
    }

    private void processMessage(Message incomeMessage, Update update) {
        Long chatId = incomeMessage.getChatId();
        String incomeMessageText = incomeMessage.getText();

        SendMessage response = null;
        List<SendMessage> postResponses = new ArrayList<>();

        boolean isCommand = commandService.isCommand(update);
        if (isCommand) {
            response = commandService.applyCommand(update);
        }

        boolean isAction = actionService.isAction(update);
        if (isAction) {
            Action action = actionService.applyAction(chatId, update);
            response = action.getResponse(chatId, update);
            postResponses = action.getPostResponses(chatId, update);
        }

        if (isNull(response)) {
            response = new SendMessage(String.valueOf(chatId), incomeMessageText);
        }

        response.setChatId(chatId);
        execute(response);
        postResponses.forEach(this::execute);
        log.info("Command was successfully executed");
    }

    public void execute(SendMessage message) {
        try {
            super.execute(message);
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