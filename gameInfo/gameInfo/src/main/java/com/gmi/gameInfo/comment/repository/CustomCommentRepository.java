package com.gmi.gameInfo.comment.repository;

import com.gmi.gameInfo.comment.domain.dto.CommentDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCommentRepository {

    List<CommentDto> findPageByNewsId(Long newsId, Pageable pageable);

}
