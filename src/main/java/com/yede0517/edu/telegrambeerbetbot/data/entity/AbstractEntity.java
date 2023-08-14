package com.yede0517.edu.telegrambeerbetbot.data.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
public abstract class AbstractEntity {
    @Id
    protected UUID id;
}