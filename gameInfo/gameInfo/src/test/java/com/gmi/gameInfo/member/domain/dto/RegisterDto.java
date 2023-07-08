package com.gmi.gameInfo.member.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;

@Schema(description = "회원가입 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    @ApiModelProperty(value = "로그인 아이디", required = true)
    @NotBlank(message = "로그인 아이디를 입력해주세요")
    private String loginId;

    @ApiModelProperty(value = "패스워드", required = true)
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @ApiModelProperty(value = "이름", required = true)
    @NotBlank(message = "성함을 입력해주세요")
    private String name;

    @ApiModelProperty(value = "닉네임", required = true)
    @NotBlank(message = "사용하실 닉네임을 입력해주세요")
    private String nickname;

    @ApiModelProperty(value = "생년월일", required = true, example = "\"20230101\"")
    @NotNull(message = "생년월일을 입력해주세요")
    @JsonFormat(pattern = "yyyyMMdd")
    @Past(message = "잘못된 날짜입니다. 다시 확인해주세요")
    private Date birthday;

    @ApiModelProperty(value = "핸드폰 번호")
    @Pattern(regexp ="^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "핸드폰 번호의 유효방식 아닙니다\n 다시 확인해주세요" )
    @NotBlank(message = "핸드폰 번호를 입력해주세요")
    private String phoneNo;

    @ApiModelProperty(value = "이메일", required = true)
    @Email(message = "이메일 형식이 아닙니다")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @ApiModelProperty(value = "이메일 인증여부", required = true)
    @NotNull(message = "이메일 인증 여부가 없습니다")
    @AssertTrue(message = "이메일 인증을 해주세요")
    private boolean boolCertifiedEmail;
}
