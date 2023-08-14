package com.yede0517.edu.telegrambeerbetbot.bot.service;

import org.springframework.stereotype.Service;

import com.yede0517.edu.telegrambeerbetbot.data.entity.GameNumberSequence;
import com.yede0517.edu.telegrambeerbetbot.repository.GameNumberSequenceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameNumberSequenceService {

    private final GameNumberSequenceRepository repository;

    public Long getGameNumber() {
        GameNumberSequence sequence = repository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Failed to ge game number sequence"));

        Long lastNumber = sequence.getLastNumber();

        sequence.setLastNumber(++lastNumber);
        repository.save(sequence);

        return lastNumber;
    }
}
