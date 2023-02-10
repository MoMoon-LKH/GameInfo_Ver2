package com.gmi.gameInfo.post.service;

import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import com.gmi.gameInfo.post.domain.dto.PostVo;


public interface PostService {

    Post save(Post post);
    boolean deleteOneById(Post post);
    void updatePost(Post post, PostDto postDto);
    PostVo findPostVoById(Long id);
}
