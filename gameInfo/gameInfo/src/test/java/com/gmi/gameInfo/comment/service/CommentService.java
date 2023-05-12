package com.gmi.gameInfo.comment.service;

import com.gmi.gameInfo.comment.domain.Comment;
import com.gmi.gameInfo.comment.domain.dto.CommentDto;
import com.gmi.gameInfo.comment.exception.NotFoundCommentException;
import com.gmi.gameInfo.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;


    @Transactional
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
    }


    @Transactional
    public void updateCommentContent(Comment comment, String updateContent) {
        comment.updateCommentContent(updateContent);
    }

    @Transactional
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public List<CommentDto> findListPageByNewsId(Long newsId, Pageable pageable) {
        return commentRepository.findPageByNewsId(newsId, pageable);
    }

    public int countByNewsId(Long newsId) {
        return commentRepository.countByNewsId(newsId);
    }

    public int maxGroupsByNewsId(Long newsId) {
        return commentRepository.maxGroupByNewsId(newsId);
    }

    public int maxSequenceByNewsIdAndGroups(Long newsId, int groups) {
        return commentRepository.maxSequenceByComment(newsId, groups);
    }
}
