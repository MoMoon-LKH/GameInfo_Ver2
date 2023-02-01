package com.gmi.gameInfo.member.domain.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@Builder
public class RegisterDto {

    @NotBlank(message = "로그인 아이디를 입력해주세요")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "성함을 입력해주세요")
    private String name;

    @NotBlank(message = "사용하실 닉네임을 입력해주세요")
    private String nickname;

    @NotNull(message = "생년월일을 입력해주세요")
    @Past(message = "잘못된 날짜입니다. 다시 확인해주세요")
    private Date birthday;

    @NotBlank(message = "핸드폰 번호를 입력해주세요")
    private String phoneNo;

    @Email(message = "이메일 형식이 아닙니다")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotNull(message = "이메일 인증 여부가 없습니다")
    @AssertTrue(message = "이메일 인증을 해주세요")
    private boolean boolCertifiedEmail;
}
