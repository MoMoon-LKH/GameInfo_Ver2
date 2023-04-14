package com.gmi.gameInfo.post.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Schema(description = "게시글 리스트 dto")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostListDto {

    private Long postId;
    private String title;
    private Long memberId;
    private String nickname;

    private int views;

    private int likes;
    private Date createDate;
}
