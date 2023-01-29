package com.gmi.gameInfo.member.service;

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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private EmailRepository emailRepository;

    @Test
    @DisplayName("이메일 인증번호 발송 테스트")
    void sendAuthNumEmail() {
    
        //given


        //when
        
        //then
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
