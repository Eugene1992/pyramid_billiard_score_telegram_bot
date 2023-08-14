package com.yede0517.edu.telegrambeerbetbot.data.entity;

import static java.lang.String.format;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("players")
@Data
@EqualsAndHashCode(callSuper = true)
public class Player extends AbstractEntity {
    private String firstName;
    private String lastName;
    private String fullName;
    private boolean isActive;

    public Player(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = format("%s %s", firstName, lastName);
    }
}