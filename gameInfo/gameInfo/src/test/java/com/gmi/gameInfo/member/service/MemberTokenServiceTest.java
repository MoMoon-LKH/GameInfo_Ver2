package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.MemberToken;
import com.gmi.gameInfo.member.repository.MemberTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberTokenServiceTest {

    @Mock
    private MemberTokenRepository memberTokenRepository;

    @InjectMocks
    private MemberTokenService memberTokenService;


    @Test
    @Rollback
    @DisplayName("MemberToken 저장")
    void saveRefreshToken() {

        //given
        MemberToken memberToken = MemberToken.builder()
                .refreshToken("test")
                .createDate(new Date())
                .build();

        //when
        given(memberTokenRepository.save(any(MemberToken.class))).willReturn(memberToken);
        MemberToken save = memberTokenService.save(memberToken);

        //then
        assertEquals(save.getRefreshToken(), memberToken.getRefreshToken());

    }

    @Test
    @Rollback
    @DisplayName("MemberToken 업데이트")
    void updateRefreshToken() {

        //given
        MemberToken memberToken = MemberToken.builder()
                .refreshToken("test")
                .createDate(new Date())
                .build();

        //when
        memberTokenService.updateRefreshToken(memberToken, "update");

        //then
        assertEquals("update", memberToken.getRefreshToken());

    }

    @Test
    @Rollback
    @DisplayName("MemberToken 삭제")
    void deleteMemberToken() {

        //given
        MemberToken memberToken = MemberToken.builder()
                .refreshToken("test")
                .createDate(new Date())
                .build();

        //when
        doNothing().when(memberTokenRepository).delete(memberToken);
        memberTokenService.delete(memberToken);

        //then
        verify(memberTokenRepository, times(1)).delete(any(MemberToken.class));

    }
}
