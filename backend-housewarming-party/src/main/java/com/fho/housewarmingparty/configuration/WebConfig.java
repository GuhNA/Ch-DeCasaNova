package com.fho.housewarmingparty.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // TODO: Alterar para o endereço do front. Ex: "http://localhost:3000"
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .exposedHeaders("Authorization")
                .allowCredentials(false); // TODO: Alterar para true quando souber endereço do front
    }
}
