package com.yede0517.edu.telegrambeerbetbot.engine.actions.player;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.PlayerService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Player;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChangePlayerAction extends AbstractGameAction implements Action {

    private final PlayerService playerService;

    @Override
    public void apply(Long chatId, Update update) {
        String text = update.getMessage().getText();
        String playerFullName = text.replaceAll(ActionIcons.ACTIVE_PLAYER_ICON, "")
                .replaceAll(ActionIcons.NOT_ACTIVE_PLAYER_ICON, "");

        Game game = gameService.getActiveGame(chatId);
        Player player = playerService.getPlayerByName(playerFullName);
        game.setActivePlayer(player.getId());

        gameService.update(game);
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        return super.getActiveFrameKeyboard(chatId);
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionIcons.ACTIVE_PLAYER_ICON) || command.contains(ActionIcons.NOT_ACTIVE_PLAYER_ICON);
    }
}
