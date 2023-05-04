package com.gmi.gameInfo.comment.repository;

import com.gmi.gameInfo.comment.domain.QComment;
import com.gmi.gameInfo.comment.domain.dto.CommentDto;
import com.gmi.gameInfo.member.domain.QMember;
import com.querydsl.core.types.Projections;
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
                                comment.content,
                                comment.createDate,
                                comment.member.id.as("memberId"),
                                comment.member.nickname,
                                comment.groups.as("groups"),
                                comment.sequence.as("sequence"),
                                comment.replyMember.nickname.as("replyNickname")
                        )
                ).from(comment)
                .leftJoin(comment.replyMember, member)
                .where(comment.news.id.eq(newsId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.groups.asc(), comment.sequence.asc())
                .fetch();
    }


}
