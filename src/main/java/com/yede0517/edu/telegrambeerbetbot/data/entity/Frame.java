package com.yede0517.edu.telegrambeerbetbot.data.entity;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Frame {
    private Integer number;
    private List<Point> points = new ArrayList<>();
    private LocalDateTime start;
    private LocalDateTime end;
    private FrameStatus status;
    private PlayerScore firstPlayerScore;
    private PlayerScore secondPlayerScore;
    private UUID winnerPlayerId;
    private List<PointStreak> streaks = new ArrayList<>();

    public Frame(Player firstPlayer, Player secondPlayer) {
        this.firstPlayerScore = new PlayerScore(firstPlayer.getId(), 0);
        this.secondPlayerScore = new PlayerScore(secondPlayer.getId(), 0);
        this.start = LocalDateTime.now();
        this.status = FrameStatus.IN_PROGRESS;
    }

    public void removeFirstPlayerPoint() {
        removePlayerPoint(firstPlayerScore.getPlayerId());
    }

    public void removeSecondPlayerPoint() {
        removePlayerPoint(secondPlayerScore.getPlayerId());
    }

    private void removePlayerPoint(UUID playerId) {
        List<Point> playerPoints = points.stream()
                .filter(point -> point.getScorerId().equals(playerId))
                .collect(Collectors.toList());

        if (!isEmpty(playerPoints)) {
            Point lastPlayerPoint = playerPoints.get(playerPoints.size() - 1);

            points.remove(lastPlayerPoint);
            recalculatePlayerScores();
        }
    }


    public void addPoint(UUID scorerId, BallType ballType) {
        int number = points.size() + 1;
        Point point = new Point(number, scorerId, ballType);
        points.add(point);
        recalculatePlayerScores();
    }

    public void recalculatePlayerScores() {
        int firstPlayerScore = 0;
        int secondPlayerScore = 0;

        for (Point point : points) {
            UUID scorerId = point.getScorerId();
            if (this.firstPlayerScore.getPlayerId().equals(scorerId)) {
                firstPlayerScore++;
            } else if (this.secondPlayerScore.getPlayerId().equals(scorerId)) {
                secondPlayerScore++;
            } else {
                throw new IllegalStateException("Illegal frame score recalculation attempt");
            }
        }

        this.firstPlayerScore.score = firstPlayerScore;
        this.secondPlayerScore.score = secondPlayerScore;

        if (isEnd()) {
            setWinner();
        }
    }

    public void setWinner() {
        Integer firstPlayerScore = this.firstPlayerScore.score;
        Integer secondPlayerScore = this.secondPlayerScore.score;

        if (firstPlayerScore == 8 || firstPlayerScore > secondPlayerScore) {
            winnerPlayerId = this.firstPlayerScore.playerId;
        }

        if (secondPlayerScore == 8  || secondPlayerScore > firstPlayerScore) {
            winnerPlayerId = this.secondPlayerScore.playerId;
        }
    }

    public boolean isEnd() {
        return firstPlayerScore.score == 8 || secondPlayerScore.score == 8 || status.equals(FrameStatus.TERMINATED);
    }

    public Point getPoint(int pointNumber) {
        return points.get(--pointNumber);
    }

    public Point getLastPoint() {
        return points.get(points.size() - 1);
    }

    public void addStreak(UUID scorerId, Integer fromPoint, Integer toPoint) {
        this.streaks.add(new PointStreak(scorerId, fromPoint, toPoint));
    }

    public PointStreak getStreak(Integer toPoint) {
        return this.streaks.stream()
                .filter(streak -> streak.getToPoint().equals(toPoint))
                .findFirst()
                .orElse(null);

    }

    public void removeStreak(PointStreak streak) {
        Integer from = streak.getFromPoint();
        Integer toPoint = streak.getToPoint();

        for (int i = from; i < toPoint; i++) {
            Point point = getPoint(i);
            point.setStreak(false);
        }

        this.streaks.remove(streak);
    }
}