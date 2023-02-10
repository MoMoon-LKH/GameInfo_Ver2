package com.gmi.gameInfo.post.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostVo {
    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private String nickname;
    private Date createDate;
    private Date updateDate;
}
