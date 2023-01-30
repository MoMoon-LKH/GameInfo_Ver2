package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.AuthEmail;
import com.gmi.gameInfo.member.repository.EmailRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("dev")
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    @DisplayName("이메일 인증번호 발송 테스트")
    void sendAuthNumEmail() {
    
        //given
        AuthEmail authEmail = new AuthEmail("rlgh28@naver.com", emailService.getAuthNum());

        //when
        boolean boolEmail = emailService.sendAuthNumberEmail(authEmail);

        //then
        assertTrue(boolEmail);
    }

    @Test
    @DisplayName("인증번호 6자리 생성")
    void createAuthNum() {

        //given
        String authNum;

        //when
        authNum = emailService.getAuthNum();

        //then
        assertEquals(6, authNum.length());
        assertInstanceOf(Integer.class, Integer.valueOf(authNum));
    }

}
