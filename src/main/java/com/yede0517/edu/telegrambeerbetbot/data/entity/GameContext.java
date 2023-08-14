package com.yede0517.edu.telegrambeerbetbot.data.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.yede0517.edu.telegrambeerbetbot.data.entity.Game;
import com.yede0517.edu.telegrambeerbetbot.data.entity.Player;
import com.yede0517.edu.telegrambeerbetbot.engine.actions.Action;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Document("game_contexts")
@Data
@EqualsAndHashCode(callSuper = true)
public class GameContext extends AbstractEntity{
    private Long gameId;
    private Player firstPlayer;
    private Player secondPlayer;
    private List<Action> actionLog;

}
