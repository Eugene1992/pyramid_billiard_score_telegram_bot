package com.yede0517.edu.telegrambeerbetbot.bot.service;

import org.springframework.stereotype.Service;

import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.GameStatus;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Player;
import com.yede0517.edu.telegrambeerbetbot.data.entity.PlayerScore;
import com.yede0517.edu.telegrambeerbetbot.repository.GameRepository;
import com.yede0517.edu.telegrambeerbetbot.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository repository;

    public Player create(Player player) {
        return repository.save(player);
    }

    public Player getPlayerByName(String fullName) {
        return repository.findByFullName(fullName);
    }
}
