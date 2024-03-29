package com.gmi.gameInfo.member.domain;

import com.gmi.gameInfo.member.domain.dto.RegisterDto;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String loginId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(length = 20)
    private String nickname;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date birthday;

    @Size(max = 15)
    private String phoneNo;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @OneToOne
    @JoinColumn(name = "token_id")
    private MemberToken memberToken;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<News> news;

    private Member(RegisterDto registerDto) {
        this.loginId = registerDto.getLoginId();
        this.password = registerDto.getPassword();
        this.name = registerDto.getName();
        this.nickname = registerDto.getNickname();
        this.birthday = registerDto.getBirthday();
        this.phoneNo = registerDto.getPhoneNo();
        this.email = registerDto.getEmail();
        this.createDate = new Date();
        this.roleType = RoleType.USER;
    }

    public static Member createMember(RegisterDto registerDto) {
        return new Member(registerDto);
    }

    public void updateMemberToken(MemberToken memberToken) {
        this.memberToken = memberToken;
    }
}
