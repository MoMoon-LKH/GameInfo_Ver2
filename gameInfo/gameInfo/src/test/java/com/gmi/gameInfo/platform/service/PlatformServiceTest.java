package com.gmi.gameInfo.platform.service;

import com.gmi.gameInfo.platform.domain.Platform;
import com.gmi.gameInfo.platform.repository.PlatformRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PlatformServiceTest {

    @Mock
    PlatformRepository platformRepository;

    @InjectMocks
    PlatformService platformService;

    @Test
    @DisplayName("단일 조회 - 성공")
    void findById() {

        //given
        Platform platform = Platform.builder()
                .id(1L)
                .name("platform")
                .build();

        given(platformRepository.findById(any(Long.class))).willReturn(Optional.of(platform));

        //when
        Platform find = platformService.findById(1L);

        //then
        assertSame(find, platform);
    }
}
