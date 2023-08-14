package com.yede0517.edu.telegrambeerbetbot.data.table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameStats {
    private String paramName;
    private String firstPlayerScored;
    private String secondPlayerScored;
}
