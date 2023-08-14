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
        /*message.setText("test");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton rackShot = new InlineKeyboardButton("С разбоя");
        InlineKeyboardButton abrikolShot = new InlineKeyboardButton("Абриколь");
        InlineKeyboardButton doubleShot = new InlineKeyboardButton("Дуплет");
        InlineKeyboardButton longShot = new InlineKeyboardButton("Дальний");
        InlineKeyboardButton frenchShot = new InlineKeyboardButton("Француз");
        InlineKeyboardButton systemShot = new InlineKeyboardButton("Система");

        a.setCallbackData("setSpecialBallType:game_68a5dc28-7e4d-441b-8c0c-6d21f855fad2_frame_1_point_1_type_system");
        b.setCallbackData("b callback");
        c.setCallbackData("c callback");
        d.setCallbackData("d callback");

        inlineKeyboardMarkup.setKeyboard(asList(
                asList(a, b),
                asList(c, d))
        );
        message.setReplyMarkup(inlineKeyboardMarkup);*/

        return message;
    }
}