package com.yede0517.edu.telegrambeerbetbot.data.entity;

import static java.util.Objects.nonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Document("games")
@Data
@EqualsAndHashCode(callSuper = true)
public class Game extends AbstractEntity {

    private Long gameId;
    private Long gameNumber;
    private Player firstPlayer;
    private Player secondPlayer;
    private PlayerScore firstPlayerScore;
    private PlayerScore secondPlayerScore;
    private LocalDateTime start;
    private LocalDateTime end;
    private GameStatus status;
    private GameType type;
    private List<Frame> frames = new ArrayList<>();

    public Game(Long gameId) {
        this.gameId = gameId;
        this.status = GameStatus.QUEUED;
    }

    public Frame getActiveFrame() {
        return frames.stream()
                .filter(frame -> frame.getStatus().equals(FrameStatus.IN_PROGRESS))
                .findFirst()
                .orElse(null);
    }

    public Player getActivePlayer() {
        if (!firstPlayer.isActive() && !secondPlayer.isActive()) {
            firstPlayer.setActive(true);
        }
        return firstPlayer.isActive() ? firstPlayer : secondPlayer;
    }

    public void setActivePlayer(UUID playerId) {
        if (firstPlayer.getId().equals(playerId)) {
            firstPlayer.setActive(true);
            secondPlayer.setActive(false);
        } else if (secondPlayer.getId().equals(playerId)) {
            secondPlayer.setActive(true);
            firstPlayer.setActive(false);
        } else {
            throw new IllegalStateException("Illegal player set active flag attempt");
        }
    }


    public void addFrame(Frame frame) {
        frame.setNumber(frames.size() + 1);
        this.frames.add(frame);
    }

    public void setPlayer(Player player) {
        if (firstPlayer == null) {
            this.firstPlayer = player;
            this.firstPlayerScore = new PlayerScore(firstPlayer.getId(), 0);
        } else if (secondPlayer == null) {
            this.secondPlayer = player;
            this.secondPlayerScore = new PlayerScore(secondPlayer.getId(), 0);
        }
    }

    public boolean isPlayersSet() {
        return firstPlayer != null && secondPlayer != null;
    }

    public boolean isContainPlayer(Player player) {
        UUID playerId = player.getId();

        return Stream.of(firstPlayer, secondPlayer)
                .filter(Objects::nonNull)
                .anyMatch(currPlayer -> currPlayer.getId().equals(playerId));
    }

    public void recalculatePlayerScores() {
        int firstPlayerScore = 0;
        int secondPlayerScore = 0;

        for (Frame frame : frames) {
            UUID winnerId = frame.getWinnerPlayerId();

            if (nonNull(winnerId)) {
                if (this.firstPlayerScore.getPlayerId().equals(winnerId)) {
                    firstPlayerScore++;
                } else if (this.secondPlayerScore.getPlayerId().equals(winnerId)) {
                    secondPlayerScore++;
                } else {
                    throw new IllegalStateException("Illegal frame score recalculation attempt");
                }
            }
        }

        this.firstPlayerScore.score = firstPlayerScore;
        this.secondPlayerScore.score = secondPlayerScore;
    }

    public Frame getFrame(int frameNumber) {
        return frames.get(--frameNumber);
    }

    public Frame getLastFrame() {
        return frames.get(frames.size() - 1);
    }
}