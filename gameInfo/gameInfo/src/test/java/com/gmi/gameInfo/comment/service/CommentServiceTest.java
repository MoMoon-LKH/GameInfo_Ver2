package com.gmi.gameInfo.comment.service;

import com.gmi.gameInfo.comment.domain.Comment;
import com.gmi.gameInfo.comment.domain.dto.CommentCreateDto;
import com.gmi.gameInfo.comment.domain.dto.CommentDto;
import com.gmi.gameInfo.comment.exception.NotFoundCommentException;
import com.gmi.gameInfo.comment.repository.CommentRepository;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.RoleType;
import com.gmi.gameInfo.news.domain.News;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    private Member member = Member.builder()
            .id(1L)
            .loginId("test")
            .password("test")
            .email("test")
            .phoneNo("test")
            .name("test")
            .birthday(new Date())
            .nickname("test")
            .roleType(RoleType.USER)
            .build();

    private News news = News.builder()
            .id(1L)
            .title("test")
            .content("test")
            .member(member)
            .build();

    @Test
    @DisplayName("댓글 저장 테스트")
    void saveComment() {

        //given
        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .content("test")
                .group(0)
                .sequence(0)
                .postId(news.getId())
                .memberId(member.getId())
                .build();
        Comment comment = Comment.createNewsComment(commentCreateDto, member, news);

        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        //when
        Comment save = commentService.save(comment);

        //then
        assertSame(save, comment);
    }
    
    @Test
    @DisplayName("댓글 단일 조회 - 실패")
    void findById_Fail() {
    
        //given
        given(commentRepository.findById(any(Long.class))).willReturn(Optional.empty());

        //when

        
        //then
        assertThrows(NotFoundCommentException.class, () -> {
            commentService.findById(1L);
        });
    }
    
    @Test
    @DisplayName("댓글 단일 조회 - 성공")
    void findById_Success() {
    
        //given
        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .content("test")
                .group(0)
                .sequence(0)
                .postId(news.getId())
                .memberId(member.getId())
                .build();
        Comment comment = Comment.createNewsComment(commentCreateDto, member, news);
        given(commentRepository.findById(any(Long.class))).willReturn(Optional.of(comment));
    
        //when
        Comment find = commentService.findById(1L);
        
        //then
        assertSame(find, comment);
    }

    @Test
    @DisplayName("댓글 내용 업데이트")
    void updateCommentContent() {

        //given
        String updateContent = "update";

        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .content("test")
                .group(0)
                .sequence(0)
                .postId(news.getId())
                .memberId(member.getId())
                .build();
        Comment comment = Comment.createNewsComment(commentCreateDto, member, news);

        //when
        commentService.updateCommentContent(comment, updateContent);

        //then
        assertEquals(updateContent, comment.getContent());

    }
    
    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() {
    
        //given
        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .content("test")
                .group(0)
                .sequence(0)
                .postId(news.getId())
                .memberId(member.getId())
                .build();
        Comment comment = Comment.createNewsComment(commentCreateDto, member, news);

        doNothing().when(commentRepository).delete(any(Comment.class));
        given(commentRepository.findById(any(Long.class))).willReturn(Optional.empty());
    
        //when
        commentService.delete(comment);

        //then
        assertThrows(NotFoundCommentException.class, () -> {
            commentService.findById(1L);
        });
    }


    @Test
    @DisplayName("댓글 리스트 조회")
    void findPageByNewsId() {

        //given
        Pageable pageable = PageRequest.of(0, 30);

        List<CommentDto> commentList = new ArrayList<>();
        CommentDto dto1 = CommentDto.builder()
                .id(1L)
                .content("test1")
                .nickname("test")
                .groups(0)
                .sequence(0)
                .createDate(new Date())
                .build();
        CommentDto dto2 = CommentDto.builder()
                .id(2L)
                .content("test2")
                .nickname("test")
                .groups(1)
                .sequence(0)
                .createDate(new Date())
                .build();
        CommentDto dto3 = CommentDto.builder()
                .id(3L)
                .content("test3")
                .nickname("test")
                .groups(2)
                .sequence(0)
                .createDate(new Date())
                .build();
        commentList.add(dto1);
        commentList.add(dto2);
        commentList.add(dto3);

        given(commentRepository.findPageByNewsId(any(Long.class), any(Pageable.class))).willReturn(commentList);

        //when
        List<CommentDto> list = commentService.findListPageByNewsId(1L, pageable);

        //then
        assertEquals(3, list.size());
        assertEquals(1L, list.get(0).getId());
        assertEquals(2L, list.get(1).getId());
        assertEquals(3L, list.get(2).getId());
    }

    @Test
    @DisplayName("댓글 총 수 조회")
    void countByNewsId() {

        //given
        Long newsId = 1L;

        given(commentRepository.countByNewsId(any(Long.class))).willReturn(3);

        //when
        int count = commentService.countByNewsId(1L);

        //then
        assertEquals(3, count);
    }

    @Test
    @DisplayName("해당 뉴스의 댓글 그룹 최대값 조회")
    void maxGroupsByNewsId() {

        //given
        Long newsId = 1L;

        // 최상위 값이 0이기때문에 +1 하기위해서 -1 반환 (댓글이 없을 경우)
        given(commentRepository.maxGroupByNewsId(any(Long.class))).willReturn(-1);

        //when
        int groups = commentService.maxGroupsByNewsId(newsId);

        //then
        assertEquals(-1, groups);
    }
    
    @Test
    @DisplayName("해당 뉴스 댓글 그룹의 순서 최대값 조회")
    void maxSequenceByNewsIdAndGroups() {
    
        //given
        Long newsId = 1L;
        int groups = 0;

        given(commentRepository.maxSequenceByComment(any(Long.class), any(Integer.class))).willReturn(0);
    
        //when
        int sequence = commentService.maxSequenceByNewsIdAndGroups(newsId, groups);
        
        //then
        assertEquals(0, sequence);
    }
}
