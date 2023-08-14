package com.yede0517.edu.telegrambeerbetbot.bot.utils;

import static java.lang.String.format;
import static java.lang.String.valueOf;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public String getDuration(LocalDateTime start, LocalDateTime end) {
        long hours = ChronoUnit.HOURS.between(start, end);
        long minutes = ChronoUnit.MINUTES.between(start, end);
        long seconds = ChronoUnit.SECONDS.between(start, end);

        String formattedHours = hours < 10 ? "0" + hours : valueOf(hours);
        String formattedMinutes = minutes < 10 ? "0" + minutes : minutes > 60 ? valueOf(minutes % 60) : valueOf(minutes);
        String formattedSeconds = seconds < 10 ? "0" + seconds : seconds > 60 ? valueOf(seconds % 60) : valueOf(seconds);

        return format("%s:%s:%s", formattedHours, formattedMinutes, formattedSeconds);
    }
}
