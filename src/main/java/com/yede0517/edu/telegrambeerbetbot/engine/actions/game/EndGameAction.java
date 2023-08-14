package com.yede0517.edu.telegrambeerbetbot.engine.actions.game;

import static java.lang.String.format;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.utils.ReplyKeyboardMarkupBuilder;
import com.yede0517.edu.telegrambeerbetbot.bot.service.StatisticService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.GameStatus;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EndGameAction implements Action {

    private final GameService gameService;
    private final StatisticService statisticService;

    @Override
    public void apply(Long chatId, Update update) {
        String message = update.getMessage().getText();

        Game queuedGame = gameService.getActiveGame(chatId);
        queuedGame.setEnd(LocalDateTime.now());
        queuedGame.setStatus(GameStatus.COMPLETED);

        gameService.update(queuedGame);
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        Game completedGame = gameService.getCompletedGame(chatId);

        completedGame.setStatus(GameStatus.CLOSED);
        gameService.update(completedGame);

        String framesStatistics = statisticService.getFramesBallsTimeline(completedGame);

        String statisticTable = statisticService.getGameStatisticTable(completedGame);

        return new ReplyKeyboardMarkupBuilder("Игра завершена!\n\nСтатистика игры:")
                .text(framesStatistics)
                .markdownText(statisticTable)
                .enableMarkdown()
                .removeKeyboard()
                .build();
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionIcons.END_ACTION_ICON) && command.contains(ActionIcons.GAME_ICON);
    }
}
