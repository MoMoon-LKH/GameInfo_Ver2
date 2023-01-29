package com.gmi.gameInfo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncThreadConfig {

    @Bean
    public Executor asyncThreadTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(3); // 기본 스레드 수
        threadPoolTaskExecutor.setMaxPoolSize(10); // 최대 스레드 수 (대기 queue 가 꽉차면)
        threadPoolTaskExecutor.setQueueCapacity(50); // 최대 대기 queue 사이즈
        threadPoolTaskExecutor.setThreadNamePrefix("Executor-");
        
        return threadPoolTaskExecutor;
    }
}
