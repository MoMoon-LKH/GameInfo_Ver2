package com.gmi.gameInfo.comment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmi.gameInfo.comment.domain.dto.CommentCreateDto;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    private int commentGroups;

    @ColumnDefault("0")
    private int sequence;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    @Column(columnDefinition = "boolean default false")
    private boolean deleteYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_member_id")
    private Member replyMember;

    public Comment(CommentCreateDto dto, Member createMember, News news) {
        this.content = dto.getContent();
        this.commentGroups = dto.getGroup();
        this.sequence = 0;
        this.createDate = new Date();
        this.member = createMember;
        this.news = news;
    }

    public Comment(CommentCreateDto dto, Member createMember, Post post) {
        this.content = dto.getContent();
        this.commentGroups = dto.getGroup();
        this.sequence = 0;
        this.createDate = new Date();
        this.member = createMember;
        this.post = post;
    }


    public Comment(CommentCreateDto dto, Member createMember, News news, Member replyMember) {
        this.content = dto.getContent();
        this.commentGroups = dto.getGroup();
        this.sequence = dto.getSequence();
        this.createDate = new Date();
        this.member = createMember;
        this.news = news;
        this.replyMember = replyMember;
    }
    public Comment(CommentCreateDto dto, Member createMember, Post post, Member replyMember) {
        this.content = dto.getContent();
        this.commentGroups = dto.getGroup();
        this.sequence = dto.getSequence();
        this.createDate = new Date();
        this.member = createMember;
        this.post = post;
        this.replyMember = replyMember;
    }


    public void updateCommentContent(String content) {
        this.content = content;
        this.updateDate = new Date();
    }

    public void updateDeleteY() {
        this.deleteYn = true;
    }

    public static Comment createReplyNewsComment(CommentCreateDto dto, Member createMember, News news, Member replyMember) {
        return new Comment(dto, createMember, news, replyMember);
    }

    public static Comment createNewsComment(CommentCreateDto dto, Member createMember, News news) {
        return new Comment(dto, createMember, news);
    }

}
