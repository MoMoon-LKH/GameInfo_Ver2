package com.gmi.gameInfo.member.repository;

import com.gmi.gameInfo.member.domain.AuthEmail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;

import javax.validation.constraints.Email;

import static org.junit.jupiter.api.Assertions.*;

@DataRedisTest
public class EmailRepositoryTest {

    @Autowired
    private EmailRepository emailRepository;

    @Test
    @DisplayName("이메일 저장 (메모리 Redis)")
    void saveEmailByRedis() {

        //given
        AuthEmail email = new AuthEmail("test@test.com", "123456");

        //when
        emailRepository.save(email);

        AuthEmail saveEmail = emailRepository.findById(email.getId()).get();

        //then
        assertEquals(saveEmail.getEmail(), email.getEmail());
        assertEquals(saveEmail.getAuthNum(), email.getAuthNum());
    }
}
