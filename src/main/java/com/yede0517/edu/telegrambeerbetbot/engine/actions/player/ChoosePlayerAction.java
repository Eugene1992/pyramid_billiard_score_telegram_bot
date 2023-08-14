package com.yede0517.edu.telegrambeerbetbot.engine.actions.player;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.utils.ReplyKeyboardMarkupBuilder;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Player;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import com.yede0517.edu.telegrambeerbetbot.repository.GameRepository;
import com.yede0517.edu.telegrambeerbetbot.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChoosePlayerAction extends AbstractGameAction implements Action {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final GameService gameService;

    private Game game;

    @Override
    public void apply(Long chatId, Update update) {
        String message = update.getMessage().getText();
        String playerFullName = message.replaceAll(ActionIcons.PLAYER_ICON, "");

        Player player = playerRepository.findByFullName(playerFullName);
        game = gameService.getQueuedGame(chatId);
        game.setPlayer(player);
        gameRepository.save(game);
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        if (game.isPlayersSet()) {
            return getStartGameKeyboard(game, "Выберите:");
        } else {
            List<Player> allPlayers = playerRepository.findAll();

            List<String> playerNames = allPlayers.stream()
                    .filter(player -> !game.isContainPlayer(player))
                    .map(player -> ActionIcons.PLAYER_ICON + player.getFullName())
                    .collect(Collectors.toList());

           return new ReplyKeyboardMarkupBuilder("Пожалуйста, выберите другого игрока:")
                    .rows(playerNames)
                    .build();
        }
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionIcons.PLAYER_ICON);
    }
}
