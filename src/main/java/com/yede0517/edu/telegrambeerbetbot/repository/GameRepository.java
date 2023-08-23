package com.yede0517.edu.telegrambeerbetbot.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.GameStatus;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Player;

public interface GameRepository extends MongoRepository<Game, String> {

    List<Game> findAllByGameIdAndStatusIn(Long gameId, GameStatus... statuses);

    List<Game> findAllByGameIdAndStatus(Long gameId, GameStatus status);

    Game findByGameIdAndStatus(Long gameId, GameStatus status);

    Game findByGameNumberAndStatus(Long gameNumber, GameStatus status);

    Game findByGameIdAndStatusIn(Long gameId, GameStatus... statuses);

    Game findByGameNumber(Long gameNumber);
}