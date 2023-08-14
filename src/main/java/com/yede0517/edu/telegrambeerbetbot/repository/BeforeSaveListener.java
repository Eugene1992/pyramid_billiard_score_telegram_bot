package com.yede0517.edu.telegrambeerbetbot.repository;

import java.util.UUID;

import com.yede0517.edu.telegrambeerbetbot.data.entity.AbstractEntity;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class BeforeSaveListener extends AbstractMongoEventListener<AbstractEntity> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<AbstractEntity> event) {
        final AbstractEntity source = event.getSource();
        if (source.getId() == null) {
            source.setId(UUID.randomUUID());
        }
    }
}