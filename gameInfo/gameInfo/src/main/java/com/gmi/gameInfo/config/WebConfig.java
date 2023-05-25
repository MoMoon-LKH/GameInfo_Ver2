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
                        "http://localhost:8080",
                        "https://localhost:8080",
                        "https://gameinfo.momoon.kro.kr",
                        "http://gameinfo.momoon.kro.kr",
                        "https://www.gameinfo.momoon.kro.kr",
                        "http://www.gameinfo.momoon.kro.kr",
                        "http://gameinfo-back:8080"
                )
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true);
    }
}
