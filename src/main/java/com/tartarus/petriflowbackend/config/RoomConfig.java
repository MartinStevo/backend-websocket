package com.tartarus.petriflowbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomConfig {

    @Bean
    public RoomsHolder roomsHolder() {
        return new RoomsHolder();
    }

}
