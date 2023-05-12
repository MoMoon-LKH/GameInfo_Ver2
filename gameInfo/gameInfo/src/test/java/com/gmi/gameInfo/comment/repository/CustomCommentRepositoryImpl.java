package com.gmi.gameInfo.comment.repository;

import com.gmi.gameInfo.comment.domain.QComment;
import com.gmi.gameInfo.comment.domain.dto.CommentDto;
import com.gmi.gameInfo.member.domain.QMember;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
                                comment.content,
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
