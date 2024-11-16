package com.fho.housewarmingparty.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "HousewarmingParty", version = "1", description = "Housewarming party management API"))
public class SwaggerConfig {
}
