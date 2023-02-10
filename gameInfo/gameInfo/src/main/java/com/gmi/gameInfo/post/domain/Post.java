package com.gmi.gameInfo.post.domain;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(length = 30)
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

    public Post(PostDto postDto, Member member) {
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
        this.createDate = new Date();
        this.member = member;
    }

    public static Post createPostByDto(PostDto postDto, Member member) {
        return new Post(postDto, member);
    }

    public void updatePost(PostDto updateDto) {
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
        this.updateDate = new Date();
    }
}
