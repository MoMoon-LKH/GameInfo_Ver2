package com.gmi.gameInfo.comment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDto {

    private String content;

    private Long memberId;

    private Long postId;

    private int group;

    private int sequence;

    private Long replyMemberId;

}
