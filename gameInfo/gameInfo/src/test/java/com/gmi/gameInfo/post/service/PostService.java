package com.gmi.gameInfo.post.service;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.post.domain.dto.PostListDto;
import com.gmi.gameInfo.post.domain.dto.PostSearchDto;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PostService {

    Post save(Post post);
    Post findById(Long id);
    boolean deleteOneById(Post post);
    void updatePost(Post post, PostDto postDto);
    PostVo findPostVoById(Long id);
    boolean checkPostOwner(Post post, Member member);

    List<PostListDto> findListByCategoryIdAndPage(PostSearchDto postSearchDto, Pageable pageable);
}
