package com.gmi.gameInfo.news.domain;

import com.gmi.gameInfo.image.domain.Images;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.news.domain.dto.NewsCreateDto;
import com.gmi.gameInfo.platform.domain.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class News {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long id;

    @Column(length = 50)
    private String title;

    @Lob
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id")
    private Platform platform;

    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY)
    private List<Images> images;


    public News(NewsCreateDto dto, Member member, Platform platform) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.createDate = new Date();
        this.member = member;
        this.platform = platform;
    }

    public void updateNews(NewsCreateDto dto, Platform platform){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.platform = platform;
        this.updateDate = new Date();
    }

    public static News createNews(NewsCreateDto createDto, Member member, Platform platform) {
        return new News(createDto, member, platform);
    }

}
