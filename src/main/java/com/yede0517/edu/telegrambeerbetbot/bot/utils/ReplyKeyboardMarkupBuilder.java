package com.yede0517.edu.telegrambeerbetbot.bot.utils;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.text.TextStringBuilder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class ReplyKeyboardMarkupBuilder {

    private ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    private List<KeyboardRow> rows = new ArrayList<>();
    private SendMessage response = new SendMessage();
    private TextStringBuilder strBuilder;

    public ReplyKeyboardMarkupBuilder(String replyText) {
        keyboardMarkup.setKeyboard(rows);
        response.setReplyMarkup(keyboardMarkup);
        strBuilder = new TextStringBuilder(replyText).appendNewLine().appendNewLine();
    }

    public ReplyKeyboardMarkupBuilder text(String text) {
        strBuilder.appendln(text);

        return this;
    }

    public ReplyKeyboardMarkupBuilder markdownText(String text) {
        strBuilder.append("```").append(text).append("```").appendNewLine();

        return this;
    }

    public ReplyKeyboardMarkupBuilder row(String... buttons) {
        KeyboardRow row = new KeyboardRow();
        for (String button : buttons) {
            row.add(new KeyboardButton(button));
        }
        rows.add(row);

        return this;
    }

    public ReplyKeyboardMarkupBuilder rows(String... buttons) {
        return rows(asList(buttons));
    }

    public ReplyKeyboardMarkupBuilder rows(Collection<String> buttons) {
        for (String button : buttons) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(button));
            rows.add(row);
        }

        return this;
    }

    public ReplyKeyboardMarkupBuilder enableMarkdown() {
        response.enableMarkdown(true);

        return this;
    }

    public ReplyKeyboardMarkupBuilder removeKeyboard() {
        response.setReplyMarkup(new ReplyKeyboardRemove(true));
        return this;
    }

    public SendMessage build() {
        response.setText(strBuilder.build());

        return response;
    }
}