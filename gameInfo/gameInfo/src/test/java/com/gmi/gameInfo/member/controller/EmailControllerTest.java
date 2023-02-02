package com.gmi.gameInfo.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.member.domain.AuthEmail;
import com.gmi.gameInfo.member.service.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("dev")
public class EmailControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmailService emailService;

    @Test
    @DisplayName("인증메일 api")
    void sendEmail() throws Exception{
    
        //given
        String email = "rlgh28@naver.com";

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
    @DisplayName("이메일 확인")
    void confirmAuthEmail() {
    
        //given
    
        //when
        
        //then
    }
    
}
