package com.gmi.gameInfo.comment.domain.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "CommentDto for Create")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDto {

    @ApiModelProperty(name = "내용", required = true)
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @ApiModelProperty(name = "포스트 id", required = true)
    @NotNull(message = "게시글 아이디를 보내주세요")
    private Long postId;

    private Long memberId;

    @ApiModelProperty(name = "댓글 그룹", required = true)
    @NotNull(message = "댓글 그룹을 보내주세요")
    private int group;

    @ApiModelProperty(name = "댓글 순서", required = false)
    private int sequence;

    @ApiModelProperty(name = "답글할 멤버 아이디", required = false)
    private Long replyMemberId;

}
