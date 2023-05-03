package com.gmi.gameInfo.comment.repository;

import com.gmi.gameInfo.comment.domain.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {

    int countByNewsId(@Param("newsId") Long newsId);
}
