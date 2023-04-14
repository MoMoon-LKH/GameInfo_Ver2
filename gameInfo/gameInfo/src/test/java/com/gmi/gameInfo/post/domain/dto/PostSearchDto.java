package com.gmi.gameInfo.post.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchDto {

    private Long categoryId;
    private String searchWord;
    private String searchSelect;
}
