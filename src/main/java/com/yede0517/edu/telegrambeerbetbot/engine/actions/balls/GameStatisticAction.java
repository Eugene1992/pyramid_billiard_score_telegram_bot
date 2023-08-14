package com.yede0517.edu.telegrambeerbetbot.engine.actions.balls;

import static java.lang.String.format;
import static java.lang.String.valueOf;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.utils.ReplyKeyboardMarkupBuilder;
import com.yede0517.edu.telegrambeerbetbot.bot.service.StatisticService;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GameStatisticAction implements Action {

    private final GameService gameService;
    private final StatisticService statisticService;

    @Override
    public void apply(Long chatId, Update update) {
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        Game game = gameService.getActiveGame(chatId);

        StringBuilder builder = new StringBuilder();
        String framesStatistics = statisticService.getFramesBallsTimeline(game);
        builder.append(framesStatistics);

        String statisticTable = statisticService.getGameStatisticTable(game);
        builder.append("```").append(statisticTable).append("```");

        return new ReplyKeyboardMarkupBuilder(builder.toString())
                .enableMarkdown()
                .build();
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.contains(ActionIcons.STATISTICS_ICON);
    }
}
