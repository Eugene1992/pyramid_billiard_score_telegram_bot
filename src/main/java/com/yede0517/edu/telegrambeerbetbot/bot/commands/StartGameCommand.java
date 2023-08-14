package com.yede0517.edu.telegrambeerbetbot.bot.commands;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.stream.Collectors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.utils.ReplyKeyboardMarkupBuilder;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.GameStatus;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Player;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;

@Command("start_game")
@RequiredArgsConstructor
public class StartGameCommand implements ICommand {

    private final GameService gameService;
    private final PlayerRepository playerRepository;

    @Override
    public SendMessage apply(Update update) {
        Long chatId = update.getMessage().getChatId();

        Game activeGame = gameService.getActiveOrQueuedGame(chatId);

        if (nonNull(activeGame)) {
            GameStatus status = activeGame.getStatus();

            switch (status) {
                case IN_PROGRESS:
                    return new ReplyKeyboardMarkupBuilder("Найдена незаконченная игра для этого чата. Выберите действие:")
                            .rows(ActionIcons.CONTINUE_ACTION_ICON + " Продолжить игру",
                                    ActionIcons.STOP_ACTION_ICON + ActionIcons.GAME_ICON + " Прервать игру")
                            .build();

                case QUEUED:
                    String firstPlayerFullName = activeGame.getFirstPlayer().getFullName();
                    String secondPlayerFullName = activeGame.getSecondPlayer().getFullName();

                    return new ReplyKeyboardMarkupBuilder("Найдена ранее созданная игра в очереди:")
                            .row(
                                    ActionIcons.PLAYER_ICON + firstPlayerFullName,
                                    ActionIcons.PLAYER_ICON + secondPlayerFullName
                            )
                            .row(ActionIcons.GAME_ICON + "Начать игру")
                            .build();
                default:
                    throw new IllegalStateException("Not processed status at game start");
            }
        } else {
            Game game = new Game(chatId);
            gameService.create(game);

            List<Player> allPlayers = playerRepository.findAll();

            List<String> playerNames = allPlayers.stream()
                    .map(player -> ActionIcons.PLAYER_ICON + player.getFullName())
                    .collect(Collectors.toList());

            return new ReplyKeyboardMarkupBuilder("Пожалуйста, выберите первого игрока:")
                    .rows(playerNames)
                    .build();
        }
    }

}