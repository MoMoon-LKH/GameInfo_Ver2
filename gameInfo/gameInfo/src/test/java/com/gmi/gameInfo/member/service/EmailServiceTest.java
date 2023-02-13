package com.gmi.gameInfo.member.service;

import com.gmi.gameInfo.member.domain.AuthEmail;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import com.gmi.gameInfo.member.exception.DifferentAuthEmailNumberException;
import com.gmi.gameInfo.member.exception.DuplicateEmailException;
import com.gmi.gameInfo.member.exception.NotFoundAuthEmailException;
import com.gmi.gameInfo.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Order(1)
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

    @Order(2)
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
    @Order(3)
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
    @DisplayName("이메일 전송 전 회원 이메일 중복 조회 실패 시")
    void duplicateEmail(){

        //given
        Calendar cal = Calendar.getInstance();
        cal.set(1996,6,19);

        RegisterDto registerDto = RegisterDto.builder()
                .loginId("test")
                .password(passwordEncoder.encode("123456"))
                .name("테스트")
                .nickname("테스트 닉네임")
                .birthday(new Date(cal.getTimeInMillis()))
                .phoneNo("01012345678")
                .email("rlgh28@naver.com").build();
        Member member = Member.createMember(registerDto);
        memberRepository.save(member);

        //when
        AuthEmail email = AuthEmail.createAuthEmail("rlgh28@naver.com", emailService.getAuthNum());

        //then
        assertThrows(DuplicateEmailException.class, () -> {
            emailService.sendAndSaveAuthEmail(email);
        });

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
    @DisplayName("이메일 검증번호 확인 실패")
    void differentAuthNum() {

        //given
        AuthEmail email = AuthEmail.createAuthEmail("rlgh28@naver.com", emailService.getAuthNum());

        //when
        assertThrows(DifferentAuthEmailNumberException.class, () -> {
            emailService.confirmAuthNum(email, "test");
        });
    }


    @Test
    @Rollback
    @DisplayName("이메일 재전송 시")
    void EmailServiceTest() {

        //given
        AuthEmail email = AuthEmail.createAuthEmail("rlgh28@naver.com", emailService.getAuthNum());
        AuthEmail sendEmail = emailService.sendAndSaveAuthEmail(email);

        AuthEmail resend = AuthEmail.createAuthEmail("rlgh28@naver.com", emailService.getAuthNum());

        //when
        AuthEmail resendEmail = emailService.sendAndSaveAuthEmail(resend);

        //then
        assertEquals(resendEmail.getAuthNum(), resend.getAuthNum());
        assertEquals(emailService.findByEmail(sendEmail.getEmail()).getAuthNum(), resendEmail.getAuthNum());

    }
}
