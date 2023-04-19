package com.gmi.gameInfo.platform.repository;

import com.gmi.gameInfo.config.TestConfig;
import com.gmi.gameInfo.news.domain.dto.NewsCreateDto;
import com.gmi.gameInfo.platform.domain.Platform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
public class PlatformRepositoryTest {


    @Autowired
    PlatformRepository platformRepository;

    @Test
    @Rollback
    @DisplayName("Platform - 저장 테스트")
    void saveTest() {

        //given
        Platform platform = Platform.builder()
                .name("name")
                .build();

        //when
        Platform save = platformRepository.save(platform);

        //then
        assertSame(save, platform);
    }

    @Test
    @DisplayName("Platform - id 단일조회")
    void findByIdTest() {

        //given
        Platform platform = Platform.builder()
                .name("name")
                .build();

        platformRepository.save(platform);

        //when
        Optional<Platform> find = platformRepository.findById(platform.getId());

        //then
        assertSame(platform, find.get());
    }


}
