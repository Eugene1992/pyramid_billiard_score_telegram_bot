package com.yede0517.edu.telegrambeerbetbot.bot.service;

import static java.lang.String.format;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.GameStatus;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.repository.GameRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository repository;
    private final GameNumberSequenceService gameNumberSequenceService;

    public Game create(Game game) {
        Long gameNumber = gameNumberSequenceService.getGameNumber();
        game.setGameNumber(gameNumber);
        return repository.save(game);
    }

    public List<Game> getAll() {
        return repository.findAll();
    }

    public List<Game> saveAll(Collection<Game> games) {
        return repository.saveAll(games);
    }

    public List<Game> getAllByGameIdAndStatuses(Long gameId, GameStatus... statuses) {
        return repository.findAllByGameIdAndStatusIn(gameId, statuses);
    }

    public Game update(Game game) {
        return repository.save(game);
    }

    public Game getQueuedGame(Long gameId) {
        return repository.findByGameIdAndStatus(gameId, GameStatus.QUEUED);
    }

    public Game getActiveOrQueuedGame(Long gameId) {
        return repository.findByGameIdAndStatusIn(gameId, GameStatus.IN_PROGRESS, GameStatus.QUEUED);
    }

    public Game getActiveGame(Long gameId) {
        return repository.findByGameIdAndStatus(gameId, GameStatus.IN_PROGRESS);
    }

    public Game getCompletedGame(Long gameId) {
        return repository.findByGameIdAndStatus(gameId, GameStatus.COMPLETED);
    }

    public Game getLastTerminatedGame(Long gameId) {
        List<Game> games = repository.findAllByGameIdAndStatus(gameId, GameStatus.TERMINATED);

        return games.stream()
                .sorted(Comparator.comparing(Game::getEnd).reversed())
                .findFirst()
                .get();
    }

    public Game getByGameNumber(Long gameNumber) {
        return repository.findByGameNumber(gameNumber);
    }

    public List<String> getLastGameLabels() {
        List<Game> games = getAll();
        return games.stream()
                .filter(game -> game.getStatus().equals(GameStatus.CLOSED))
                .sorted(Comparator.comparing(Game::getStart).reversed())
                .map(game -> {
                    Long gameNumber = game.getGameNumber();
                    LocalDateTime start = game.getStart();
                    String date = start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
                    String firstPlayerName = game.getFirstPlayer().getLastName();
                    Integer firstPlayerScore = game.getFirstPlayerScore().getScore();
                    Integer secondPlayerScore = game.getSecondPlayerScore().getScore();
                    String secondPlayerName = game.getSecondPlayer().getLastName();

                    return format("%s) %s %s %s %s-%s %s", gameNumber, date, ActionIcons.GAME_HISTORY_ICON,
                            firstPlayerName, firstPlayerScore, secondPlayerScore, secondPlayerName);
                })
                .collect(Collectors.toList());
    }
}
