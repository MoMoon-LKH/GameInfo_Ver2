package com.gmi.gameInfo.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.exceptionHandler.GlobalExceptionHandler;
import com.gmi.gameInfo.member.domain.AuthEmail;
import com.gmi.gameInfo.member.domain.dto.EmailDto;
import com.gmi.gameInfo.member.exception.DifferentAuthEmailNumberException;
import com.gmi.gameInfo.member.exception.SendEmailFailException;
import com.gmi.gameInfo.member.service.EmailService;
import com.gmi.gameInfo.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class)
@ActiveProfiles("dev")
public class EmailControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @MockBean
    private MemberService memberService;

    String email = "rlgh28@naver.com";

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new EmailController(emailService, memberService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @Rollback
    @DisplayName("인증메일 api")
    void sendEmail() throws Exception{
    
        //given
        AuthEmail authEmail = AuthEmail.createAuthEmail(email , emailService.getAuthNum());
        given(emailService.sendAndSaveAuthEmail(authEmail)).willReturn(authEmail);
        EmailDto emailDto = EmailDto.builder()
                .email(email)
                .build();

        //when
        mockMvc.perform(post("/api/email/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDto)))
                .andExpect(status().isOk())
                .andDo(print());

        //then
    }

    @Test
    @Rollback
    @DisplayName("인증메일 api - 메일전송 실패시")
    void notSendEmail() throws Exception {

        //given
        EmailDto emailDto = EmailDto.builder()
                .email(email)
                .build();
        given(emailService.sendAndSaveAuthEmail(any())).willThrow(SendEmailFailException.class);

        //when
        mockMvc.perform(post("/api/email/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDto)))
                .andExpect(status().isInternalServerError())
                .andDo(print());

        //then
    }
    
    @Test
    @Rollback
    @DisplayName("인증번호 확인 - 일치")
    void confirmAuthEmail() throws Exception {
    
        //given
        String authNum = "123456";
        EmailDto emailDto = EmailDto.builder()
                .email(email)
                .authNum("123456")
                .build();

        AuthEmail authEmail = AuthEmail.builder()
                .id("1")
                .email(email)
                .authNum(authNum).build();

        given(emailService.findOneById(authEmail.getId())).willReturn(authEmail);
        given(emailService.confirmAuthNum(authEmail, authNum)).willReturn(true);

        //when
        mockMvc.perform(get("/api/email/verify-number/" + authEmail.getId())
                        .param("authNum", authNum))
                .andExpect(status().isOk())
                .andDo(print());

        //then
    }
    
    @Test
    @Rollback
    @DisplayName("인증번호 불일치 - 불일치")
    void notConfirmAuthEmail() throws Exception {

        //given

        String email = "rlgh28@naver.com";
        String authNum = "123123";
        EmailDto emailDto = EmailDto.builder()
                .email(email)
                .authNum("123456")
                .build();
        AuthEmail authEmail = AuthEmail.builder()
                .id("1")
                .email(email)
                .authNum(authNum).build();
        given(emailService.findOneById(authEmail.getId())).willReturn(authEmail);
        given(emailService.confirmAuthNum(authEmail, "123123")).willThrow(new DifferentAuthEmailNumberException());

        //when

        //then
        mockMvc.perform(get("/api/email/verify-number/" + authEmail.getId())
                        .param("authNum", authNum))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }


}
