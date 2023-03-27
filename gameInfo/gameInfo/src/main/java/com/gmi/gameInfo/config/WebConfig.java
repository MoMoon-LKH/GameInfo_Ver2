package com.gmi.gameInfo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "https://localhost:3000",
                        "https://gameinfo.momoon.kro.kr"
                )
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true);
    }
}
