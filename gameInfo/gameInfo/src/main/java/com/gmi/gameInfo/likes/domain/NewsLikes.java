package com.gmi.gameInfo.likes.domain;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.news.domain.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsLikes {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private LikeType likeType;


    public void updateLikes(LikeType type) {
        this.likeType = type;
    }

    public NewsLikes(News news, Member member, LikeType type) {
        this.news = news;
        this.member = member;
        this.likeType = type;
    }

    public static NewsLikes createNewLikes(News news, Member member, LikeType type) {
        return new NewsLikes(news, member, type);
    }
}
