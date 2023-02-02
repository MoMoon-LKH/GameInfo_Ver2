package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.AuthEmail;
import com.gmi.gameInfo.member.exception.DifferentAuthEmailNumberException;
import com.gmi.gameInfo.member.exception.NotFoundAuthEmailException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    @Rollback
    @DisplayName("이메일 인증번호 발송 테스트")
    void sendAuthNumEmail() {

        //given
        AuthEmail authEmail = AuthEmail.createAuthEmail("rlgh28@naver.com", emailService.getAuthNum());

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
        assertThat(authNum).containsOnlyDigits();
    }

    @Test
    @Rollback
    @DisplayName("이메일 전송 및 redis 저장 테스트")
    void sendAndSaveAuthEmail() {
        //given
        AuthEmail email = AuthEmail.createAuthEmail("rlgh28@naver.com", emailService.getAuthNum());

        //when
        AuthEmail sendEmail = emailService.sendAndSaveAuthEmail(email);

        //then
        assertEquals(email.getEmail(), sendEmail.getEmail());
        assertEquals(email.getAuthNum(), sendEmail.getAuthNum());

    }

    @Test
    @Rollback
    @DisplayName("이메일 검증 확인")
    void confirmAuthEmail() {
        //given
        AuthEmail email = AuthEmail.createAuthEmail("rlgh28@naver.com", emailService.getAuthNum());
        emailService.save(email);

        //then
        AuthEmail saveEmail =  emailService.findOneById(email.getId());
        boolean checkAuthNum = emailService.confirmAuthNum(saveEmail, email.getAuthNum());

        //when
        assertTrue(checkAuthNum);
    }

    @Test
    @Rollback
    @DisplayName("이메일 정보 찾지못하였을 때 Exception 검증")
    void notFoundAuthEmail() {

        //given

        //then

        //when
        assertThrows(NotFoundAuthEmailException.class, () -> {
            emailService.findOneById(0L);
        });
    }

    @Test
    @Rollback
    @DisplayName("이메일 검증 확인 실패")
    void differentAuthNum() {

        //given
        AuthEmail email = AuthEmail.createAuthEmail("rlgh28@naver.com", emailService.getAuthNum());

        //when
        assertThrows(DifferentAuthEmailNumberException.class, () -> {
            emailService.confirmAuthNum(email, "test");
        });
    }
}
