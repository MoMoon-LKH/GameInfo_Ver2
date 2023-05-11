package com.gmi.gameInfo.member.domain.dto;

import com.gmi.gameInfo.member.domain.Member;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "회원 간단 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSimpleDto {

    @ApiModelProperty(value = "회원 id")
    private Long id;

    @ApiModelProperty(value = "이름")
    private String name;

    @ApiModelProperty(value = "닉네임")
    private String nickname;


    public MemberSimpleDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.nickname = member.getNickname();
    }
}
