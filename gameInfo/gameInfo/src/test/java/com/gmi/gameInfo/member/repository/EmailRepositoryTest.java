package com.gmi.gameInfo.member.repository;

import com.gmi.gameInfo.member.domain.AuthEmail;
import com.gmi.gameInfo.member.exception.NotFoundAuthEmailException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.annotation.Rollback;

import javax.validation.constraints.Email;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataRedisTest
public class EmailRepositoryTest {

    @Autowired
    private EmailRepository emailRepository;

    @Test
    @Rollback
    @DisplayName("이메일 저장 (메모리 Redis)")
    void saveEmailByRedis() {

        //given
        AuthEmail email = AuthEmail.createAuthEmail("test@test.com", "123456");

        //when
        emailRepository.save(email);

        AuthEmail saveEmail = emailRepository.findById(email.getId()).get();

        //then
        assertEquals(saveEmail.getEmail(), email.getEmail());
        assertEquals(saveEmail.getAuthNum(), email.getAuthNum());
    }
    
    @Test
    @Rollback
    @DisplayName("이메일 조회")
    void findAuthEmailById() {
    
        //given
        AuthEmail email = AuthEmail.createAuthEmail("test@test.com", "123456");
        emailRepository.save(email);

        //when
        Optional<AuthEmail> saveEmail = emailRepository.findById(email.getId());
        
        //then
        assertEquals(email.getEmail(), saveEmail.get().getEmail());
        assertEquals(email.getAuthNum(), saveEmail.get().getAuthNum());
    }

    @Test
    @Rollback
    @DisplayName("이메일 조회 - email")
    void findAuthEmailByEmail() {

        //given
        AuthEmail email = AuthEmail.createAuthEmail("test@test.com", "123456");
        emailRepository.save(email);

        //when
        AuthEmail saveEmail = emailRepository
                .findAuthEmailByEmail(email.getEmail()).orElseThrow(NotFoundAuthEmailException::new);

        //then
        System.out.println(saveEmail.toString());
        assertEquals(email.getEmail(), saveEmail.getEmail());
        assertEquals(email.getAuthNum(), saveEmail.getAuthNum());

    }


    @Test
    @Rollback
    @DisplayName("해당 이메일 삭제")
    void deleteAllByEmail() {

        //given
        AuthEmail email = AuthEmail.createAuthEmail("test@testj.com", "123456");
        emailRepository.save(email);

        //when
        emailRepository.deleteById(email.getId());

        //then
        assertThrows(NotFoundAuthEmailException.class, () -> {
            emailRepository.findAuthEmailByEmail(email.getEmail()).orElseThrow(NotFoundAuthEmailException::new);
        });
    }

}
