package com.gmi.gameInfo.member.repository;

import com.gmi.gameInfo.member.domain.MemberToken;
import com.gmi.gameInfo.member.exception.NotFoundRefreshTokenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MemberTokenRepositoryTest {
    
    @Autowired
    MemberTokenRepository tokenRepository;
    
    @Test
    @Rollback
    @DisplayName("토큰 등록")
    void saveToken() {
    
        //given
        MemberToken token = MemberToken.builder()
                .refreshToken("test")
                .createDate(new Date())
                .build();
    
        //when
        MemberToken save = tokenRepository.save(token);

        //then
        assertEquals(save.getRefreshToken(), token.getRefreshToken());
    }
    
    @Test
    @Transactional
    @DisplayName("토큰정보 업데이트")
    void updateToken() throws Exception {
    
        //given
        MemberToken token = MemberToken.builder()
                .refreshToken("test")
                .createDate(new Date())
                .build();
        MemberToken save = tokenRepository.save(token);

        //when
        save.updateRefreshToken("test2");
        MemberToken save2 = tokenRepository.findById(save.getId()).orElseThrow(ChangeSetPersister.NotFoundException::new);

        //then
        assertEquals(save.getRefreshToken(), save2.getRefreshToken());
        
    }
    
    @Test
    @DisplayName("토큰정보가 삭제")
    void deleteToken() {
    
        //given
        MemberToken token = MemberToken.builder()
                .refreshToken("test")
                .createDate(new Date())
                .build();
        MemberToken save = tokenRepository.save(token);

        //when
        tokenRepository.delete(save);

        //then
        assertThrows(NotFoundRefreshTokenException.class, () -> {
            tokenRepository.findById(save.getId()).orElseThrow(NotFoundRefreshTokenException::new);
        });
    }

}
