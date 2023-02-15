package com.gmi.gameInfo.member.domain.dto;

import com.gmi.gameInfo.member.domain.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "회원 id")
    private Long id;
    
    @ApiModelProperty(value = "로그인 아이디")
    private String loginId;
    
    @ApiModelProperty(value = "이름")
    private String name;
    
    @ApiModelProperty(value = "닉네임")
    private String nickname;

    @ApiModelProperty(value = "생년월일")
    private Date birthday;

    @ApiModelProperty(value = "핸드폰 번호")
    private String phoneNo;

    @ApiModelProperty(value = "이메일")
    private String email;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.birthday = member.getBirthday();
        this.phoneNo = member.getPhoneNo();
        this.email = member.getEmail();
    }
}
