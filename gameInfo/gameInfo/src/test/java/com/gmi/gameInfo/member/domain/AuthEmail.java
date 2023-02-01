package com.gmi.gameInfo.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "email", timeToLive = 300)
public class AuthEmail {

    @Id
    private Long id;
    private String email;
    private String authNum;
    private LocalDateTime createTime;

    private AuthEmail(String email, String authNum) {
        this.email = email;
        this.authNum = authNum;
        this.createTime = LocalDateTime.now();
    }

    public static AuthEmail createAuthEmail(String email, String authNum) {
        return new AuthEmail(email, authNum);
    }

}
