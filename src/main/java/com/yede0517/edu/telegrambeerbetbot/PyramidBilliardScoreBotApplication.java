package com.yede0517.edu.telegrambeerbetbot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.yede0517.edu.telegrambeerbetbot.bot.BotRunner;
import com.yede0517.edu.telegrambeerbetbot.repository.GameNumberSequenceRepository;
import com.yede0517.edu.telegrambeerbetbot.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class PyramidBilliardScoreBotApplication implements CommandLineRunner {

    private final BotRunner botRunner;
    private final PlayerRepository repository;
    private final GameNumberSequenceRepository sequenceRepository;


    public static void main(String[] args) throws TelegramApiException {
        SpringApplication.run(PyramidBilliardScoreBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        sequenceRepository.save(new GameNumberSequence(1L));
//        repository.save(new Player("Евгений Дейнека"));
//        repository.save(new Player("Антон Панфилов"));
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(botRunner);
    }
}
