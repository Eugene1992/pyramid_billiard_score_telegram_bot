package com.yede0517.edu.telegrambeerbetbot.bot.commands;

import static java.util.Arrays.asList;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import lombok.RequiredArgsConstructor;

@Command("test")
@RequiredArgsConstructor
public class TestCommand implements ICommand {

    @Override
    public SendMessage apply(Update update) {
        SendMessage message = new SendMessage();

        return message;
    }
}