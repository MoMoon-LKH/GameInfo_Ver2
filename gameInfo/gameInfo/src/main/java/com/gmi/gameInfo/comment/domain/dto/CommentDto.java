package com.gmi.gameInfo.comment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    private String content;

    private Date createDate;

    private Long memberId;

    private String nickname;

    private int sort;

    private String replyNickname;

}
