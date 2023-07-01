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

import java.util.ArrayList;
import java.util.List;
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
    @Rollback
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

    @Test
    @Rollback
    @DisplayName("Platform - List<Long> id에 해당되는 List 조회")
    void findByIdIn() {
    
        //given
        Platform platform = Platform.builder()
                .name("name1")
                .build();

        platformRepository.save(platform);

        Platform platform2 = Platform.builder()
                .name("name2")
                .build();

        platformRepository.save(platform2);

        Platform platform3 = Platform.builder()
                .name("name3")
                .build();

        platformRepository.save(platform2);

        List<Long> list = new ArrayList<>();
        list.add(platform.getId());
        list.add(platform2.getId());

        //when
        List<Platform> find = platformRepository.findAllByIdIn(list);

        //then
        assertEquals(2, find.size());

    }

}
