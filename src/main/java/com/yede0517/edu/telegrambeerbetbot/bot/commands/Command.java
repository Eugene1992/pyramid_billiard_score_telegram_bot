package com.yede0517.edu.telegrambeerbetbot.bot.commands;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

@Component
public @interface Command {
    @AliasFor(
            annotation = Component.class
    )
    String value();
}
