package com.gmi.gameInfo.member.domain;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "email", timeToLive = 300)
@ToString
public class AuthEmail {

    @Id
    private Long id;

    @Indexed
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
