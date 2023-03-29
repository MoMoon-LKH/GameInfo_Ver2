package com.gmi.gameInfo.post.repository;

import com.gmi.gameInfo.category.domain.QCategory;
import com.gmi.gameInfo.post.domain.QPost;
import com.gmi.gameInfo.post.domain.dto.PostListDto;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory factory;

    private QPost post = QPost.post;
    private QCategory category = QCategory.category;

    @Override
    public Optional<PostVo> findPostVoById(Long id) {

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

    @Override
    public List<PostListDto> findAllByCategoryIdAndPage(Long categoryId, Pageable pageable) {


        return factory
                .select(
                        Projections.bean(PostListDto.class,
                                post.id.as("postId"),
                                post.title,
                                post.member.id.as("memberId"),
                                post.member.nickname,
                                post.createDate
                                )
                ).from(post)
                .where(
                        post.category.id.eq(categoryId)
                )
                .orderBy(post.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
