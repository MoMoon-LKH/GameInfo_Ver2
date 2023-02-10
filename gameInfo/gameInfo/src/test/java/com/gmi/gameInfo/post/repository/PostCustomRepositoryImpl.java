package com.gmi.gameInfo.post.repository;

import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.gmi.gameInfo.post.domain.QPost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory factory;

    @Override
    public Optional<PostVo> findPostVoById(Long id) {

        QPost post = QPost.post;

        return Optional.ofNullable(factory
                .select(
                        Projections.bean(PostVo.class,
                                post.id,
                                post.title,
                                post.content,
                                post.member.id.as("memberId"),
                                post.member.nickname.as("nickname"),
                                post.createDate,
                                post.updateDate
                        )
                ).from(post)
                .where(
                        post.id.eq(id)
                ).fetchOne());

    }
}
