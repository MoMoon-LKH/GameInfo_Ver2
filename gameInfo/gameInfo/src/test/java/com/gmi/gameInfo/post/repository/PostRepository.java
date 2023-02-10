package com.gmi.gameInfo.post.repository;

import com.gmi.gameInfo.post.domain.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    int deletePostById(@Param("id") Long id);
}
