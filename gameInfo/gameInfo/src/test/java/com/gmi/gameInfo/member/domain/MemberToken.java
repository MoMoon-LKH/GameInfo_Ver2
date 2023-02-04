package com.gmi.gameInfo.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;


    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
