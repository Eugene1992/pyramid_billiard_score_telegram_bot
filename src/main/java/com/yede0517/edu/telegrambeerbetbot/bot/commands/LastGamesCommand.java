package com.yede0517.edu.telegrambeerbetbot.bot.commands;

import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.utils.ReplyKeyboardMarkupBuilder;
import lombok.RequiredArgsConstructor;

@Command("last_games")
@RequiredArgsConstructor
public class LastGamesCommand implements ICommand {

    private final GameService gameService;

    @Override
    public SendMessage apply(Update update) {
        List<String> gameLabels = gameService.getLastGameLabels();

        return new ReplyKeyboardMarkupBuilder("Последние игры:")
                .rows(gameLabels)
                .build();
    }
}