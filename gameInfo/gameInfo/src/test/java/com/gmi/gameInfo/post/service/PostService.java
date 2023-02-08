package com.gmi.gameInfo.post.service;

import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.exception.FailDeletePostException;
import com.gmi.gameInfo.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public boolean deleteOneById(Post post) {
        int delete = postRepository.deletePostById(post.getId());

        if(delete < 1) {
            throw new FailDeletePostException();
        }

        return true;
    }
}
