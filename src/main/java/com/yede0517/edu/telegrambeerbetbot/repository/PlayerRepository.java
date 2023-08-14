package com.yede0517.edu.telegrambeerbetbot.repository;

import com.yede0517.edu.telegrambeerbetbot.data.entity.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, String> {

    Player findByFullName(String fullName);
}