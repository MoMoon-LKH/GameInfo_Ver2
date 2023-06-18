package com.gmi.gameInfo.comment.repository;

import com.gmi.gameInfo.comment.domain.QComment;
import com.gmi.gameInfo.comment.domain.dto.CommentDto;
import com.gmi.gameInfo.member.domain.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository{

    private final JPAQueryFactory factory;

    private QComment comment = QComment.comment;

    private QMember member = QMember.member;

    @Override
    public List<CommentDto> findPageByNewsId(Long newsId, Pageable pageable) {

        return factory.select(
                        Projections.bean(CommentDto.class,
                                comment.id,
                                new CaseBuilder()
                                        .when(comment.deleteYn.eq(true))
                                        .then("삭제된 댓글입니다")
                                        .otherwise(comment.content)
                                        .as("content")
                                ,
                                comment.createDate,
                                comment.member.id.as("memberId"),
                                comment.member.nickname,
                                comment.commentGroups.as("groups"),
                                comment.sequence.as("sequence"),
                                member.nickname.as("replyNickname")
                        )
                ).from(comment)
                .leftJoin(comment.replyMember, member)
                .where(comment.news.id.eq(newsId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.commentGroups.asc(), comment.sequence.asc())
                .fetch();
    }


    @Override
    public int maxGroupByNewsId(Long newsId) {
        return factory.select(
                        comment.commentGroups.max().coalesce(-1)
                ).from(comment)
                .where(comment.news.id.eq(newsId))
                .fetchFirst();
    }

    @Override
    public int maxSequenceByComment(Long newsId, int groups) {
        return factory.select(
                        comment.sequence.max().coalesce(-1)
                ).from(comment)
                .where(comment.news.id.eq(newsId)
                        .and(comment.commentGroups.eq(groups)))
                .fetchFirst();
    }
}
