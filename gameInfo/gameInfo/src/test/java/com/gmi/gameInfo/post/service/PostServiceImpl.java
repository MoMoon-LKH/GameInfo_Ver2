package com.gmi.gameInfo.post.service;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.post.domain.dto.PostListDto;
import com.gmi.gameInfo.post.domain.dto.PostSearchDto;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import com.gmi.gameInfo.post.exception.FailDeletePostException;
import com.gmi.gameInfo.post.exception.NotFoundPostException;
import com.gmi.gameInfo.post.exception.NotPostOwnerException;
import com.gmi.gameInfo.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;


    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
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


    @Override
    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(NotFoundPostException::new);
    }

    public PostVo findPostVoById(Long id) {
        return postRepository.findPostVoById(id).orElseThrow(NotFoundPostException::new);
    }

    @Override
    public boolean checkPostOwner(Post post, Member member) {

        if (!post.getMember().getId().equals(member.getId())) {
            throw new NotPostOwnerException();
        }

        return true;
    }

    @Override
    public List<PostListDto> findListByCategoryIdAndPage(PostSearchDto postSearchDto, Pageable pageable) {
        return postRepository.findAllByCategoryIdAndPage(postSearchDto, pageable);
    }
}
