package com.yede0517.edu.telegrambeerbetbot.engine.actions.balls;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.utils.ReplyKeyboardMarkupBuilder;
import com.yede0517.edu.telegrambeerbetbot.bot.service.StatisticService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GameHistoryAction extends AbstractGameAction implements Action {

    private final GameService gameService;
    private final StatisticService statisticService;


    @Override
    public void apply(Long chatId, Update update) {
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        String text = update.getMessage().getText();
        int end = text.indexOf(")");
        String gameNumber = text.substring(0, end);

        Game completedGame = gameService.getByGameNumber(Long.valueOf(gameNumber));

        String framesStatistics = statisticService.getFramesBallsTimeline(completedGame);

        String statisticTable = statisticService.getGameStatisticTable(completedGame);

        return new ReplyKeyboardMarkupBuilder("Статистика игры:")
                .text(framesStatistics)
                .markdownText(statisticTable)
                .enableMarkdown()
                .removeKeyboard()
                .build();
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionIcons.GAME_HISTORY_ICON);
    }
}
