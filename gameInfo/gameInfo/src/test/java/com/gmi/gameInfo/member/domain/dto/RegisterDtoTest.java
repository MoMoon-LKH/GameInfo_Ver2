package com.gmi.gameInfo.member.domain.dto;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterDtoTest {

    private ValidatorFactory factory;
    private Validator validator;

    @BeforeEach
    public void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    @DisplayName("loginId 빈문자열 체크")
    void blankLoginId() {

        //given
        RegisterDto dto = RegisterDto.builder()
                .loginId("")
                .password("test")
                .name("test")
                .nickname("nickname")
                .email("email@naver.com")
                .phoneNo("01323232323")
                .birthday(Date.valueOf(LocalDate.of(1996,6,19)))
                .boolCertifiedEmail(true)
                .build();

        // when then
        validateMessage(dto,"로그인 아이디를 입력해주세요");

    }

    @Test
    @DisplayName("password 빈문자열 체크")
    void blankPassword() {

        //given
        RegisterDto dto = RegisterDto.builder()
                .loginId("test")
                .password("")
                .name("test")
                .nickname("nickname")
                .email("email@naver.com")
                .phoneNo("01323232323")
                .birthday(Date.valueOf(LocalDate.of(1996,6,19)))
                .boolCertifiedEmail(true)
                .build();

        // when then
        validateMessage(dto,"비밀번호를 입력해주세요");
    }

    @Test
    @DisplayName("name 빈문자열 체크")
    void blankName() {

        //given
        RegisterDto dto = RegisterDto.builder()
                .loginId("test")
                .password("test")
                .name("")
                .nickname("nickname")
                .email("email@naver.com")
                .phoneNo("01323232323")
                .birthday(Date.valueOf(LocalDate.of(1996,6,19)))
                .boolCertifiedEmail(true)
                .build();

        // when then
        validateMessage(dto,"성함을 입력해주세요");
    }

    @Test
    @DisplayName("nickname 빈 문자열 체크")
    void blankNickname() {

        //given
        RegisterDto dto = RegisterDto.builder()
                .loginId("test")
                .password("test")
                .name("test")
                .nickname("")
                .email("email@naver.com")
                .phoneNo("01323232323")
                .birthday(Date.valueOf(LocalDate.of(1996,6,19)))
                .boolCertifiedEmail(true)
                .build();

        // when then
        validateMessage(dto,"사용하실 닉네임을 입력해주세요");
    }

    @Test
    @DisplayName("birthday null 체크")
    void checkNullBirthday() {

        //given
        RegisterDto dto = RegisterDto.builder()
                .loginId("test")
                .password("test")
                .name("test")
                .nickname("test")
                .email("email@naver.com")
                .phoneNo("01323232323")
                .boolCertifiedEmail(true)
                .build();

        // when then
        validateMessage(dto,"생년월일을 입력해주세요");
    }

    @Test
    @DisplayName("birthday 과거 날짜가 아니라면")
    void notPastBirthday() {

        //given
        RegisterDto dto = RegisterDto.builder()
                .loginId("test")
                .password("test")
                .name("test")
                .nickname("test")
                .email("email@naver.com")
                .birthday(Date.valueOf(LocalDate.of(2099,6,19)))
                .phoneNo("01323232323")
                .boolCertifiedEmail(true)
                .build();

        // when then
        validateMessage(dto,"잘못된 날짜입니다. 다시 확인해주세요");
    }

    @Test
    @DisplayName("phoneNo 빈 문자열 체크")
    void blankPhoneNo() {

        //given
        RegisterDto dto = RegisterDto.builder()
                .loginId("test")
                .password("test")
                .name("test")
                .nickname("test")
                .email("email@naver.com")
                .birthday(Date.valueOf(LocalDate.of(1996,6,19)))
                .phoneNo("")
                .boolCertifiedEmail(true)
                .build();

        // when then
        validateMessage(dto,"핸드폰 번호를 입력해주세요");
    }

    @Test
    @DisplayName("email 빈 문자열 체크")
    void blankEmail() {

        //given
        RegisterDto dto = RegisterDto.builder()
                .loginId("test")
                .password("test")
                .name("test")
                .nickname("test")
                .email("")
                .birthday(Date.valueOf(LocalDate.of(1996,6,19)))
                .phoneNo("01323232323")
                .boolCertifiedEmail(true)
                .build();

        // when then
        validateMessage(dto,"이메일을 입력해주세요");
    }

    @Test
    @DisplayName("email 형식 체크")
    void checkEmailFormat() {

        //given
        RegisterDto dto = RegisterDto.builder()
                .loginId("test")
                .password("test")
                .name("test")
                .nickname("test")
                .email("asdf")
                .birthday(Date.valueOf(LocalDate.of(1996,6,19)))
                .phoneNo("01323232323")
                .boolCertifiedEmail(true)
                .build();

        // when then
        validateMessage(dto,"이메일 형식이 아닙니다");
    }
    
    @Test
    @DisplayName("이메일 인증 체크")
    void checkCertifiedEmail() {

        //given
        RegisterDto dto = RegisterDto.builder()
                .loginId("test")
                .password("test")
                .name("test")
                .nickname("test")
                .email("test@test.colm")
                .birthday(Date.valueOf(LocalDate.of(1996,6,19)))
                .phoneNo("01323232323")
                .boolCertifiedEmail(false)
                .build();

        // when then
        validateMessage(dto,"이메일 인증을 해주세요");
    }


    @Test
    @DisplayName("핸드폰 번호 형식 체크")
    void phoneNumberCheck_Worst() {

        //given
        String worst = "010232422132";

        //when
        RegisterDto dto = RegisterDto.builder()
                .loginId("test")
                .password("test")
                .name("test")
                .nickname("test")
                .email("test@test.colm")
                .birthday(Date.valueOf(LocalDate.of(1996,6,19)))
                .phoneNo(worst)
                .boolCertifiedEmail(true)
                .build();

        //then
        validateMessage(dto, "핸드폰 번호의 유효방식 아닙니다\n 다시 확인해주세요");
    }


    void validateMessage(RegisterDto dto, String message) {
        //when
        Set<ConstraintViolation<RegisterDto>> validate = validator.validate(dto);

        //then
        assertThat(validate).isNotEmpty();
        validate
                .forEach(error -> {
                    assertEquals(error.getMessage(), message);
                });
    }

}
