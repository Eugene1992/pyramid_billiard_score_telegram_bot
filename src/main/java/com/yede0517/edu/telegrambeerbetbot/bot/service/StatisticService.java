package com.yede0517.edu.telegrambeerbetbot.bot.service;

import static com.yede0517.edu.telegrambeerbetbot.bot.utils.Utils.*;
import static com.yede0517.edu.telegrambeerbetbot.bot.utils.tablebuilder.TableCellAlignmentType.*;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yede0517.edu.telegrambeerbetbot.bot.utils.tablebuilder.TableTemplate;
import com.yede0517.edu.telegrambeerbetbot.data.entity.BallType;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Frame;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.GameStatus;
import com.yede0517.edu.telegrambeerbetbot.data.entity.GameType;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Player;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Point;
import com.yede0517.edu.telegrambeerbetbot.data.entity.PointStreak;
import com.yede0517.edu.telegrambeerbetbot.data.shared.ActionIcons;
import com.yede0517.edu.telegrambeerbetbot.data.table.GameStats;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final GameService gameService;

    public String getGameOverallStatistic(String gameNumber) {
        Game game = gameService.getByGameNumber(Long.valueOf(gameNumber));

        return getGameOverallStatistic(game);
    }

    public String getGameOverallStatistic(Game game) {
        StringBuilder builder = new StringBuilder();

        String framesStatistics = getFramesBallsTimeline(game);
        builder.append(framesStatistics);

        String statisticTable = getGameStatisticTable(game);
        builder.append(statisticTable);

        return builder.toString();
    }


    public String getFramesBallsTimeline(Game game) {
        boolean isGameActive = game.getStatus().equals(GameStatus.IN_PROGRESS);

        Player firstPlayer = game.getFirstPlayer();
        UUID firstPlayerId = firstPlayer.getId();

        Player secondPlayer = game.getSecondPlayer();
        UUID secondPlayerId = secondPlayer.getId();

        StringBuilder frameBuilder = new StringBuilder();
        LocalDateTime start = game.getStart();
        LocalDateTime end = game.getEnd();

        String firstPlayerFullName = firstPlayer.getFullName();
        String secondPlayerFullName = secondPlayer.getFullName();


        String gameDate = start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        Integer firstPlayerFrameScore = game.getFirstPlayerScore().getScore();
        Integer secondPlayerFrameScore = game.getSecondPlayerScore().getScore();
        String gameScore = format("%s Счет: %s %s-%s %s\n", ActionIcons.SCORE_ICON, firstPlayerFullName, firstPlayerFrameScore,
                secondPlayerFrameScore, secondPlayerFullName);
        frameBuilder.append(gameScore);

        GameType gameType = game.getType();
        if (nonNull(gameType)) {
            String typeName = gameType.getName();
            frameBuilder.append(String.format("%sТип: %s\n", ActionIcons.GAME_TYPE_ICON, typeName));
        }
        frameBuilder.append(format("%s Дата: %s\n", ActionIcons.CALENDAR_ICON, gameDate));

        if (!isGameActive) {
            String gameDuration = getDuration(start, end);
            frameBuilder.append(format("%s Продолжительность: %s\n\n", ActionIcons.CLOCK_ICON, gameDuration));
        } else {
            frameBuilder.append("\n");
        }

        List<Frame> frames = game.getFrames();
        for (int i = 0, j = 1; i < frames.size(); i++, j++) {
            Frame frame = frames.get(i);
            frameBuilder.append(String.format(ActionIcons.FRAME_ICON + " Партия: %s\n", j));

            UUID winnerPlayerId = frame.getWinnerPlayerId();
            if (nonNull(winnerPlayerId)) {
                String winnerFullName = winnerPlayerId.equals(firstPlayerId) ?
                        firstPlayerFullName : secondPlayerFullName;
                frameBuilder.append(String.format(ActionIcons.WINNER_PLAYER_ICON + " Победитель: %s\n", winnerFullName));
            }

            LocalDateTime frameStart = frame.getStart();
            LocalDateTime frameEnd = frame.getEnd();
            if (nonNull(frameStart) && nonNull(frameEnd)) {
                String frameDuration = getDuration(frameStart, frameEnd);
                frameBuilder.append(ActionIcons.CLOCK_ICON).append(" Время: ").append(frameDuration).append("\n\n");
            }

            List<Point> points = frame.getPoints();

            StringBuilder firstPlayerBuilder = new StringBuilder();
            StringBuilder secondPlayerBuilder = new StringBuilder();

            for (Point point : points) {
                BallType ballType = point.getBallType();
                UUID playerId = point.getScorerId();

                if (firstPlayerId.equals(playerId)) {
                    firstPlayerBuilder.append(ballType.getIcon());
                } else {
                    firstPlayerBuilder.append("➖");
                }

                if (secondPlayerId.equals(playerId)) {
                    secondPlayerBuilder.append(ballType.getIcon());
                } else {
                    secondPlayerBuilder.append("➖");
                }
            }
            frameBuilder.append(firstPlayerBuilder).append("\n");
            frameBuilder.append(secondPlayerBuilder).append("\n").append("\n");
        }

        Map<UUID, List<PointStreak>> playerStreaks = game.getFrames()
                .stream()
                .flatMap(frame -> frame.getStreaks().stream())
                .collect(Collectors.groupingBy(PointStreak::getScorerId, Collectors.toList()));

        List<PointStreak> firstPlayerPointStreaks = playerStreaks.get(firstPlayerId);
        if (nonNull(firstPlayerPointStreaks)) {
            List<Integer> firstPlayerStreakNumbers = firstPlayerPointStreaks
                    .stream()
                    .map(streak -> streak.getToPoint() - streak.getFromPoint() + 1)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            frameBuilder.append("Cерии:\n")
                    .append(String.format("\n%s%s:\n", ActionIcons.PLAYER_ICON, firstPlayerFullName));

            for (Integer streakNumber : firstPlayerStreakNumbers) {
                for (int i = 0; i < streakNumber; i++) {
                    frameBuilder.append(ActionIcons.STREAK_BALL_ICON);
                }
                frameBuilder.append("\n");
            }
        }


        List<PointStreak> secondPlayerPointStreaks = playerStreaks.get(secondPlayerId);
        if (nonNull(secondPlayerPointStreaks)) {
            List<Integer> secondPlayerStreakNumbers = secondPlayerPointStreaks
                    .stream()
                    .map(streak -> streak.getToPoint() - streak.getFromPoint() + 1)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            frameBuilder.append(String.format("\n%s%s:\n", ActionIcons.PLAYER_ICON, secondPlayerFullName));

            for (Integer streakNumber : secondPlayerStreakNumbers) {
                for (int i = 0; i < streakNumber; i++) {
                    frameBuilder.append(ActionIcons.STREAK_BALL_ICON);
                }
                frameBuilder.append("\n");
            }
        }


        return frameBuilder.toString();
    }

    public String getGameStatisticTable(Game game) {
        String firstPlayerName = game.getFirstPlayer().getFullName();
        String secondPlayerName = game.getSecondPlayer().getFullName();

        Integer firstPlayerFrameScore = game.getFirstPlayerScore().getScore();
        Integer secondPlayerFrameScore = game.getSecondPlayerScore().getScore();
        List<GameStats> data = asList(
                new GameStats(ActionIcons.FRAME_ICON + " Партии:", valueOf(firstPlayerFrameScore), valueOf(secondPlayerFrameScore)),
                calculateGameStatsForBallType(game, ActionIcons.FOREIGN_BALL_ICON + " Чужие:", BallType.FOREIGN),
                calculateGameStatsForBallType(game, ActionIcons.OWN_BALL_ICON + " Свояки:", BallType.OWN),
                calculateGameStatsForBallType(game, ActionIcons.PENALTY_BALL_ICON + " Штрафы:", BallType.PENALTY),
                calculateGameStatsForBallType(game, ActionIcons.PRESENT_BALL_ICON + " Подставы:", BallType.PRESENT),
                calculateGameStatsForBallType(game, ActionIcons.DUMMY_BALL_ICON + " Дураки:", BallType.DUMMY),
                calculateGameStatsForBallTotal(game, ActionIcons.NOTES_ICON + " Всего:")
        );

        String draw = new TableTemplate<>(data)
                .addColumn("", GameStats::getParamName, LEFT)
                .addColumn(firstPlayerName, GameStats::getFirstPlayerScored, CENTER)
                .addColumn(secondPlayerName, GameStats::getSecondPlayerScored, CENTER)
                .draw();


        return draw;
    }

    private GameStats calculateGameStatsForBallType(Game game, String paramName, BallType ballType) {
        return calculateGameStatsForBallType(game, paramName, ballType, false);
    }

    private GameStats calculateGameStatsForBallTotal(Game game, String paramName) {
        List<Frame> frames = game.getFrames();
        UUID firstPlayerId = game.getFirstPlayer().getId();
        UUID secondPlayerId = game.getSecondPlayer().getId();

        long firstPlayerTotal = calculatePlayerTotalBallsCount(frames, firstPlayerId);
        long secondPlayerTotal = calculatePlayerTotalBallsCount(frames, secondPlayerId);

        return new GameStats(
                paramName,
                valueOf(firstPlayerTotal),
                valueOf(secondPlayerTotal)
        );
    }

    private GameStats calculateGameStatsForBallType(Game game, String paramName, BallType ballType, boolean omitPercentage) {
        List<Frame> frames = game.getFrames();
        UUID firstPlayerId = game.getFirstPlayer().getId();
        UUID secondPlayerId = game.getSecondPlayer().getId();

        long firstPlayerTotal = calculatePlayerTotalBallsCount(frames, firstPlayerId);
        long secondPlayerTotal = calculatePlayerTotalBallsCount(frames, secondPlayerId);

        long firstPlayerBallTypeCount = calculateBallType(frames, ballType, firstPlayerId);
        float firstPlayerBallTypePercent = calculateRateFloat(firstPlayerBallTypeCount, firstPlayerTotal);

        long secondPlayerBallTypeCount = calculateBallType(frames, ballType, secondPlayerId);
        float secondPlayerBallTypePercent = calculateRateFloat(secondPlayerBallTypeCount, secondPlayerTotal);

        String template = omitPercentage ? "%s" : "%s (%s%%)";

        return new GameStats(
                paramName,
                format(template, firstPlayerBallTypeCount, firstPlayerBallTypePercent),
                format(template, secondPlayerBallTypeCount, secondPlayerBallTypePercent)
        );
    }

    private float calculateRateFloat(long part, long total) {
        if (total == 0) {
            return total;
        }
        final DecimalFormat decimalFormat = new DecimalFormat("#.##",
                DecimalFormatSymbols.getInstance(Locale.US));
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        String value = decimalFormat.format(part * 100d / total);
        return new BigDecimal(value).setScale(1, RoundingMode.HALF_DOWN).floatValue();
    }

    private long calculateBallType(List<Frame> frames, BallType ballType, UUID playerId) {
        return frames.stream()
                .flatMap(frame -> frame.getPoints().stream())
                .filter(point -> point.getScorerId().equals(playerId) && point.getBallType().equals(ballType))
                .count();

    }

    private long calculatePlayerTotalBallsCount(List<Frame> frames, UUID playerId) {
        return frames.stream()
                .flatMap(frame -> frame.getPoints().stream())
                .filter(point -> point.getScorerId().equals(playerId))
                .count();
    }
}
