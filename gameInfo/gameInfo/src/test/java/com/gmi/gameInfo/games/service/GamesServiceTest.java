package com.gmi.gameInfo.games.service;

import com.gmi.gameInfo.games.domain.GamesPlatform;
import com.gmi.gameInfo.games.repository.GamesGenreRepository;
import com.gmi.gameInfo.games.repository.GamesPlatformRepository;
import com.gmi.gameInfo.games.repository.GamesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class GamesServiceTest {

    @Mock
    private GamesRepository gamesRepository;

    @Mock
    private GamesPlatformRepository gamesPlatformRepository;

    @Mock
    private GamesGenreRepository gamesGenreRepository;

    @InjectMocks
    private GamesService gamesService;

    @Test
    @DisplayName("게임 저장 - (Genre || Platform) List가 size 0인 상태로 들어갈 경우")
    void save_EmptyGenreOrPlatformList() {
        
    }
    
    
    @Test
    @DisplayName("게임 저장 - 성공")
    void save_success() {
    
        //given
    
        //when
        
        //then
    }
    
    
}
