package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.MemberToken;
import com.gmi.gameInfo.member.repository.MemberTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberTokenServiceTest {

    @Mock
    private MemberTokenRepository memberTokenRepository;

    @InjectMocks
    private MemberTokenService memberTokenService;


    @Test
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
}
