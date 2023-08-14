package com.yede0517.edu.telegrambeerbetbot.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.GameNumberSequence;

public interface GameNumberSequenceRepository extends MongoRepository<GameNumberSequence, UUID> {
}