package com.fho.housewarmingparty.configuration;

import java.util.TimeZone;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeZoneConfiguration {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
