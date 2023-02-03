package com.gmi.gameInfo.member.domain.dto;

import com.gmi.gameInfo.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String name;
    private String nickname;
    private Date birthday;
    private String phoneNo;
    private String email;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.birthday = member.getBirthday();
        this.phoneNo = member.getPhoneNo();
        this.email = member.getEmail();
    }
}
