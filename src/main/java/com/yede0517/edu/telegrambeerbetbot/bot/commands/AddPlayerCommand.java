package com.yede0517.edu.telegrambeerbetbot.bot.commands;

import org.apache.commons.text.TextStringBuilder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yede0517.edu.telegrambeerbetbot.bot.service.PlayerService;
import com.yede0517.edu.telegrambeerbetbot.bot.utils.ReplyKeyboardMarkupBuilder;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Player;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import lombok.RequiredArgsConstructor;

@Command("add_player")
@RequiredArgsConstructor
public class AddPlayerCommand implements ICommand {

    private final PlayerService playerService;

    private static final String CORRECT_COMMAND_REGEX = "\\/add_player \"[a-zA-Z0-9_ ]{3,25}\"";

    @Override
    public SendMessage apply(Update update) {
        String text = update.getMessage().getText();
        String responseText;

        if (text.matches(CORRECT_COMMAND_REGEX)) {
            int start = text.indexOf("\"");
            int end = text.lastIndexOf("\"");
            String arg = text.substring(++start, end);

            String[] tokens = arg.split(" ");
            String firstName = tokens[0];
            String lastName = tokens[1];

            Player newPlayer = new Player(firstName, lastName);
            playerService.create(newPlayer);

            responseText = "Игрок был успешно добавлен в систему";
        } else {
            TextStringBuilder strBuilder = new TextStringBuilder();

            responseText = strBuilder.append(ActionIcons.DOUBLE_EXPLANATION_MARK_ICON).appendln("Команда введена неверно.")
                    .appendln("Пожалуйста, введите команду используя формат:")
                    .appendln("/add_player \"Имя игрока\"\n")
                    .appendln("Требования к имени игрока:")
                    .appendln("- имя может содержать большие и маленькие символи, пробелы и нижнее подчеркивание")
                    .appendln("- длина имени не должна быть не менее 3 и не более 25 символов")
                    .build();
        }

        return new ReplyKeyboardMarkupBuilder(responseText).build();
    }
}