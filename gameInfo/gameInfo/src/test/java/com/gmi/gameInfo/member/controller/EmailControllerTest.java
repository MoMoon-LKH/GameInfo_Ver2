package com.gmi.gameInfo.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.exceptionHandler.GlobalExceptionHandler;
import com.gmi.gameInfo.member.domain.AuthEmail;
import com.gmi.gameInfo.member.exception.DifferentAuthEmailNumberException;
import com.gmi.gameInfo.member.exception.SendEmailFailException;
import com.gmi.gameInfo.member.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
    String email = "rlgh28@naver.com";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new EmailController(emailService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("인증메일 api")
    void sendEmail() throws Exception{
    
        //given
        AuthEmail authEmail = AuthEmail.createAuthEmail(email , emailService.getAuthNum());
        given(emailService.sendAndSaveAuthEmail(authEmail)).willReturn(authEmail);

        //when
        mockMvc.perform(post("/api/email/authenticate")
                        .param("email", email))
                .andExpect(status().isOk())
                .andDo(print());

        //then
    }

    @Test
    @DisplayName("인증메일 api - 메일전송 실패시")
    void notSendEmail() throws Exception {

        //given
        given(emailService.sendAndSaveAuthEmail(any())).willThrow(SendEmailFailException.class);

        //when
        mockMvc.perform(post("/api/email/authenticate")
                        .param("email", email))
                .andExpect(status().isInternalServerError())
                .andDo(print());

        //then
    }
    
    @Test
    @DisplayName("인증번호 확인 - 일치")
    void confirmAuthEmail() throws Exception {
    
        //given
        String authNum = "123456";

        AuthEmail authEmail = AuthEmail.builder()
                .id(1L)
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
    @DisplayName("인증번호 불일치 - 불일치")
    void notConfirmAuthEmail() throws Exception {

        //given

        String email = "rlgh28@naver.com";
        String authNum = "123456";

        AuthEmail authEmail = AuthEmail.builder()
                .id(1L)
                .email(email)
                .authNum(authNum).build();
        given(emailService.findOneById(authEmail.getId())).willReturn(authEmail);
        given(emailService.confirmAuthNum(authEmail, "123123")).willThrow(new DifferentAuthEmailNumberException());

        //when

        //then
        mockMvc.perform(get("/api/email/verify-number/" + authEmail.getId())
                        .param("authNum", "123123"))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }


}
