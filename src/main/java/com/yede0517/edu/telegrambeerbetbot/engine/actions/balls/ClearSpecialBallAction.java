package com.yede0517.edu.telegrambeerbetbot.engine.actions.balls;

import static com.yede0517.edu.telegrambeerbetbot.data.shared.InlineActionCallbacks.CLEAR_SPECIAL_BALL_ACTION_PREFIX;
import static java.lang.Integer.parseInt;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.GameService;
import com.yede0517.edu.telegrambeerbetbot.bot.service.KeyboardService;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.AbstractGameAction;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClearSpecialBallAction extends AbstractGameAction implements Action {

    private final GameService gameService;
    private final KeyboardService keyboardService;

    @Override
    public void apply(Long chatId, Update update) {
        String command = update.getMessage().getText();

        Map<String, String> properties = getCallbackProperties(command, CLEAR_SPECIAL_BALL_ACTION_PREFIX);

        String gameId = properties.get("game");
        String frameNumber = properties.get("frame");
        String pointNumber = properties.get("point");

        gameService.clearSpecialBallType(gameId, frameNumber, pointNumber);
    }

    @Override
    public SendMessage getResponse(Long chatId, Update update) {
        return keyboardService.getActiveFrameKeyboard(chatId, "Пометка была успешно очищена!");
    }

    @Override
    public boolean isAppropriate(String command) {
        return command.startsWith(CLEAR_SPECIAL_BALL_ACTION_PREFIX);
    }
}
