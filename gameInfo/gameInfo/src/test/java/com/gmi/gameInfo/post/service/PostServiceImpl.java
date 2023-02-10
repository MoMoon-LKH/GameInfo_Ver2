package com.gmi.gameInfo.post.service;

import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import com.gmi.gameInfo.post.exception.FailDeletePostException;
import com.gmi.gameInfo.post.exception.NotFoundPostException;
import com.gmi.gameInfo.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;


    public Post save(Post post) {
        return postRepository.save(post);
    }

    public PostVo findPostVoById(Long id) {
        return postRepository.findPostVoById(id).orElseThrow(NotFoundPostException::new);
    }

    @Transactional
    public boolean deleteOneById(Post post) {
        int delete = postRepository.deletePostById(post.getId());

        if(delete < 1) {
            throw new FailDeletePostException();
        }

        return true;
    }

    @Transactional
    public void updatePost(Post post, PostDto postDto) {
        post.updatePost(postDto);
    }

}
