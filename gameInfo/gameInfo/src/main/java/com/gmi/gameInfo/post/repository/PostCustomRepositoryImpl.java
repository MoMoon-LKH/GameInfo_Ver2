package com.gmi.gameInfo.post.repository;

import com.gmi.gameInfo.category.domain.QCategory;
import com.gmi.gameInfo.post.domain.QPost;
import com.gmi.gameInfo.post.domain.dto.PostListDto;
import com.gmi.gameInfo.post.domain.dto.PostSearchDto;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
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
    public List<PostListDto> findAllByCategoryIdAndPage(PostSearchDto postSearchDto, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        String searchWord = postSearchDto.getSearchWord();
        StringExpression resultTitle = Expressions.stringTemplate("'[' || {0} || '] ' || {1}", post.category.name, post.title);
        Long categoryId = postSearchDto.getCategoryId();

        builder.and(
            post.category.id.eq(categoryId)
                .or(post.category.parentId.eq(categoryId))
        );

        if (searchWord != null && !searchWord.isEmpty()) {
            String searchSelect = postSearchDto.getSearchSelect();

            if (searchSelect != null && searchSelect.equals("writer")) {
                builder.and(post.member.nickname.containsIgnoreCase(searchWord));
            } else {
                builder.and(post.title.containsIgnoreCase(searchWord));
            }
        }

        return factory
                .select(
                        Projections.bean(PostListDto.class,
                                post.id.as("postId"),
                                resultTitle.as("title"),
                                post.member.id.as("memberId"),
                                post.member.nickname,
                                post.createDate
                                )
                ).from(post)
                .where(
                        builder
                )
                .orderBy(post.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

}
