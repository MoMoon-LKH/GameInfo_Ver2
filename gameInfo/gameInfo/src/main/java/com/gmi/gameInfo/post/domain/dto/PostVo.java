package com.gmi.gameInfo.post.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Schema(description = "포스트 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostVo {

    @ApiModelProperty(value = "포스트 id")
    private Long id;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "내용")
    private String content;
    
    @ApiModelProperty(value = "작성자 id")
    private Long memberId;
    
    @ApiModelProperty(value = "작성자 닉네임")
    private String nickname;
    
    @ApiModelProperty(value = "포스트 작성일자")
    private Date createDate;
    
    @ApiModelProperty(value = "포스트 수정일자")
    private Date updateDate;
}
