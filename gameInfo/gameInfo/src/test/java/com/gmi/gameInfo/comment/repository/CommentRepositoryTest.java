package com.gmi.gameInfo.comment.repository;

import com.gmi.gameInfo.comment.domain.Comment;
import com.gmi.gameInfo.comment.domain.dto.CommentCreateDto;
import com.gmi.gameInfo.comment.domain.dto.CommentDto;
import com.gmi.gameInfo.comment.exception.NotFoundCommentException;
import com.gmi.gameInfo.config.TestConfig;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.RoleType;
import com.gmi.gameInfo.member.repository.MemberRepository;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.repository.NewsRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback
@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    NewsRepository newsRepository;

    Member member;

    News news;

    @BeforeAll
    void setUp() {
        member = Member.builder()
                .name("test")
                .loginId("test")
                .password("test")
                .nickname("test")
                .phoneNo("test")
                .email("test")
                .birthday(new Date())
                .roleType(RoleType.USER)
                .build();
        memberRepository.save(member);


        news = News.builder()
                .content("test")
                .title("test")
                .member(member)
                .createDate(new Date())
                .build();
        newsRepository.save(news);
    }


    @Test
    @DisplayName("댓글 저장")
    void saveComment() {
    
        //given
        CommentCreateDto dto = CommentCreateDto.builder()
                .content("test")
                .group(0)
                .sequence(0)
                .postId(1L)
                .build();

        Comment comment = Comment.createNewsComment(dto, member, news);

    
        //when
        Comment save = commentRepository.save(comment);

        //then
        assertSame(comment, save);
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() {

        //given
        CommentCreateDto dto = CommentCreateDto.builder()
                .content("test")
                .group(0)
                .sequence(0)
                .postId(1L)
                .build();
        Comment comment = Comment.createNewsComment(dto, member, news);
        commentRepository.save(comment);

        //when
        commentRepository.delete(comment);

        //then
        assertThrows(NotFoundCommentException.class, () -> {
            commentRepository.findById(comment.getId()).orElseThrow(NotFoundCommentException::new);
        });
    }


    @Test
    @DisplayName("댓글 단일 조회")
    void findById() {

        //given
        CommentCreateDto dto = CommentCreateDto.builder()
                .content("test")
                .group(1)
                .sequence(0)
                .postId(1L)
                .build();
        Comment comment = Comment.createNewsComment(dto, member, news);
        commentRepository.save(comment);

        //when
        Comment find = commentRepository.findById(comment.getId()).orElse(null);

        //then
        assertEquals(1, find.getCommentGroups());
        assertEquals("test", find.getContent());

    }
    
    @Test
    @DisplayName("댓글 리스트 조회 - page")
    void findPageByNewsId() {
    
        //given
        Pageable pageable = PageRequest.of(0, 30);
        CommentCreateDto dto = CommentCreateDto.builder()
                .content("test")
                .group(0)
                .sequence(0)
                .postId(1L)
                .build();
        Comment comment = Comment.createNewsComment(dto, member, news);
        commentRepository.save(comment);

        //when
        List<CommentDto> list = commentRepository.findPageByNewsId(news.getId(), pageable);

        //then
        assertEquals(1, list.size());
        assertEquals(dto.getContent(), list.get(0).getContent());
    }


    @Test
    @DisplayName("댓글 리스트 조회 - 순서 보장")
    void findPageByNewsIdSequence() {

        //given
        Pageable pageable = PageRequest.of(0, 30);
        CommentCreateDto dto = CommentCreateDto.builder()
                .content("test")
                .group(0)
                .postId(1L)
                .build();
        Comment comment = Comment.createNewsComment(dto, member, news);
        commentRepository.save(comment);

        CommentCreateDto dto2 = CommentCreateDto.builder()
                .content("test2")
                .group(0)
                .sequence(1)
                .postId(1L)
                .build();
        Comment comment2 = Comment.createReplyNewsComment(dto2, member, news, member);
        commentRepository.save(comment2);

        CommentCreateDto dto3 = CommentCreateDto.builder()
                .content("test3")
                .group(0)
                .sequence(2)
                .postId(1L)
                .build();
        Comment comment3 = Comment.createReplyNewsComment(dto3, member, news, member);
        commentRepository.save(comment3);

        CommentCreateDto dto4 = CommentCreateDto.builder()
                .content("test4")
                .group(1)
                .postId(1L)
                .build();
        Comment comment4 = Comment.createNewsComment(dto4, member, news);
        commentRepository.save(comment4);

        CommentCreateDto dto5 = CommentCreateDto.builder()
                .content("test5")
                .group(0)
                .sequence(3)
                .postId(1L)
                .build();
        Comment comment5 = Comment.createReplyNewsComment(dto5, member, news, member);
        commentRepository.save(comment5);

        //when
        List<CommentDto> list = commentRepository.findPageByNewsId(news.getId(), pageable);


        //then
        assertEquals(5, list.size());

        assertEquals(0, list.get(0).getGroups());
        assertEquals(0, list.get(0).getSequence());

        assertEquals(0, list.get(1).getGroups());
        assertEquals(1, list.get(1).getSequence());

        assertEquals(0, list.get(2).getGroups());
        assertEquals(2, list.get(2).getSequence());

        assertEquals(0, list.get(3).getGroups());
        assertEquals(3, list.get(3).getSequence());

        assertEquals(1, list.get(4).getGroups());
        assertEquals(0, list.get(4).getSequence());

    }

    @Test
    @DisplayName("댓글 삭제 내용 확인 - deleteY")
    void confirmContentByDeleteY() {

        //given
        Pageable pageable = PageRequest.of(0, 30);
        CommentCreateDto dto = CommentCreateDto.builder()
                .content("test")
                .group(0)
                .postId(1L)
                .build();
        Comment comment = Comment.createNewsComment(dto, member, news);
        comment.updateDeleteY();
        commentRepository.save(comment);

        //when
        List<CommentDto> find = commentRepository.findPageByNewsId(news.getId(), pageable);

        //then
        assertEquals("삭제된 댓글입니다", find.get(0).getContent());

    }
    
    @Test
    @DisplayName("댓글 수 조회 - newsId")
    void countByNewsId() {
    
        //given
        CommentCreateDto dto = CommentCreateDto.builder()
                .content("test")
                .group(0)
                .postId(1L)
                .build();
        Comment comment = Comment.createNewsComment(dto, member, news);
        commentRepository.save(comment);

        CommentCreateDto dto2 = CommentCreateDto.builder()
                .content("test2")
                .group(0)
                .sequence(1)
                .postId(1L)
                .build();
        Comment comment2 = Comment.createReplyNewsComment(dto2, member, news, member);
        commentRepository.save(comment2);
    
        //when
        int total = commentRepository.countByNewsId(news.getId());
        
        //then
        assertEquals(2, total);
    }


    @Test
    @DisplayName("댓글 그룹의 최대값 조회 - 댓글이 없을 경우")
    void maxCommentGroupByNewsId_Empty() {

        //given

        //when
        int max = commentRepository.maxGroupByNewsId(news.getId());

        //then
        assertEquals(-1, max);
    }

    @Test
    @DisplayName("댓글 그룹의 최대값 조회")
    void maxCommentGroupByNewsId() {

        //given
        CommentCreateDto dto = CommentCreateDto.builder()
                .content("test1")
                .group(0)
                .postId(1L)
                .build();
        Comment comment = Comment.createNewsComment(dto, member, news);
        commentRepository.save(comment);
        CommentCreateDto dto2 = CommentCreateDto.builder()
                .content("test2")
                .group(1)
                .postId(1L)
                .build();
        Comment comment2 = Comment.createNewsComment(dto2, member, news);
        commentRepository.save(comment2);

        //when
        int maxGroups = commentRepository.maxGroupByNewsId(news.getId());

        //then
        assertEquals(1, maxGroups);
    }

    
    @Test
    @DisplayName("댓글 그룹의 순서 최대값 조회 - 없을 경우")
    void maxSequenceByNewsIdAndGroups_Empty() {
    
        //given

        //when
        int sequence = commentRepository.maxSequenceByComment(news.getId(), 0);

        //then
        assertEquals(-1, sequence);
    }

    @Test
    @DisplayName("댓글 그룹의 순서 최대값 조회")
    void maxSequenceByNewsIdAndGroups() {

        //given
        CommentCreateDto dto = CommentCreateDto.builder()
                .content("test1")
                .group(0)
                .sequence(0)
                .postId(1L)
                .build();
        Comment comment = Comment.createReplyNewsComment(dto, member, news, member);
        commentRepository.save(comment);
        CommentCreateDto dto2 = CommentCreateDto.builder()
                .content("test2")
                .group(0)
                .sequence(1)
                .postId(1L)
                .build();
        Comment comment2 = Comment.createReplyNewsComment(dto2, member, news, member);
        commentRepository.save(comment2);

        //when
        int sequence = commentRepository.maxSequenceByComment(news.getId(), 0);

        //then
        assertEquals(1, sequence);
    }
}
