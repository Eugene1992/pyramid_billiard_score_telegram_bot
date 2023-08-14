package com.yede0517.edu.telegrambeerbetbot.bot.commands;

import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.utils.ReplyKeyboardMarkupBuilder;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.GameStatus;
import lombok.RequiredArgsConstructor;

@Command("terminate_all_active")
@RequiredArgsConstructor
public class TerminateAllActiveGamesCommand implements ICommand {

    private final GameService gameService;

    @Override
    public SendMessage apply(Update update) {
        Long chatId = update.getMessage().getChatId();

        List<Game> activeGames = gameService.getAllByGameIdAndStatuses(chatId,
                GameStatus.IN_PROGRESS, GameStatus.QUEUED);

        activeGames.forEach(game -> game.setStatus(GameStatus.TERMINATED));

        gameService.saveAll(activeGames);

        return new ReplyKeyboardMarkupBuilder("Все активные игры для этого чата были остановлены")
                .removeKeyboard()
                .build();
    }
}