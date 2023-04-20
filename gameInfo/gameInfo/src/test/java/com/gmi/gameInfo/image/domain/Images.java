package com.gmi.gameInfo.image.domain;

import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Images {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;

    private String fileName;

    private String extension;

    private String path;

    private Date createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;


    public void updateImages(Images images) {
        this.originalName = images.getOriginalName();
        this.fileName = images.getFileName();
        this.extension = images.getExtension();
        this.path = images.getPath();
    }

    public void updateRelationPost(Post post) {
        this.post = post;
    }

    public void updateRelationNews(News news) {
        this.news = news;
    }
}
