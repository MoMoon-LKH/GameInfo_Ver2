package com.gmi.gameInfo.post.repository;

import com.gmi.gameInfo.category.domain.Category;
import com.gmi.gameInfo.category.repository.CategoryRepository;
import com.gmi.gameInfo.config.TestConfig;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.RoleType;
import com.gmi.gameInfo.member.repository.MemberRepository;
import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostListDto;
import com.gmi.gameInfo.post.domain.dto.PostSearchDto;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.gmi.gameInfo.post.exception.NotFoundPostException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
@Rollback
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostCustomRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private Member member;
    private Member member2;
    private Category category;

    private Category childCategory;

    @BeforeAll
     void setUp(){
        member = Member.builder()
                .loginId("test232314")
                .birthday(new Date())
                .nickname("test")
                .name("name")
                .email("email424141")
                .password("password")
                .roleType(RoleType.USER)
                .build();
        memberRepository.save(member);
        member2 = Member.builder()
                .loginId("test112321425")
                .birthday(new Date())
                .nickname("member")
                .name("name")
                .email("email1232323")
                .password("password")
                .roleType(RoleType.USER)
                .build();
        memberRepository.save(member2);

        category = Category.builder()
                .name("test")
                .build();
        categoryRepository.save(category);
        childCategory = Category.builder()
                .name("test2")
                .parentId(category.getId())
                .build();
        categoryRepository.save(childCategory);
    }
    
    @Test
    @DisplayName("PostVo 단일 조회 - 실패")
    void findPostVoById_Fail() {
    
        //given
        Long id = 1L;
    
        //when
        Optional<PostVo> find = postRepository.findPostVoById(id);

        //then
        assertThrows(NotFoundPostException.class, () -> {
            PostVo postVo = find.orElseThrow(NotFoundPostException::new);
        });
    }
    
    
    @Test
    @DisplayName("PostVo 단일 조회 - 성공")
    void findPostVoById_Success() {
    
        //given
        Post post = postRepository.save(
                Post.builder()
                        .title("test")
                        .content("content")
                        .createDate(new Date())
                        .member(member)
                        .build()
        );

        //when
        PostVo postVo = postRepository.findPostVoById(post.getId()).orElseThrow(NotFoundPostException::new);
        
        //then
        assertEquals(post.getId(), postVo.getId());
    }

    @Test
    @DisplayName("PostListDto 리스트 조회")
    void findAllByCategoryIdAndPage() {

        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long categoryId = category.getId();

        PostSearchDto postSearchDto = PostSearchDto.builder()
                .categoryId(categoryId)
                .build();

        Post post = Post.builder()
                .title("test")
                .content("test")
                .createDate(new Date())
                .category(category)
                .member(member).build();
        postRepository.save(post);

        //when
        List<PostListDto> postList = postRepository.findAllByCategoryIdAndPage(postSearchDto, pageable);

        //then
        assertEquals(1, postList.size());

    }

    @Test
    @DisplayName("PostListDto 리스트 검색 조회 - 검색값이 없을 시")
    void notSearch_findAllCategoryAndPage() {

        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long categoryId = category.getId();

        PostSearchDto postSearchDto = PostSearchDto.builder()
                .categoryId(categoryId)
                .build();

        Post post = Post.builder()
                .title("test1")
                .content("test")
                .createDate(new Date())
                .category(category)
                .member(member).build();
        postRepository.save(post);

        Post post2 = Post.builder()
                .title("test")
                .content("test")
                .createDate(new Date())
                .category(category)
                .member(member).build();
        postRepository.save(post2);

        //when
        List<PostListDto> postListDtos = postRepository.findAllByCategoryIdAndPage(postSearchDto, pageable);


        //then
        assertEquals(2, postListDtos.size());
    }

    @Test
    @DisplayName("PostListDto 리스트 검색 조회 - 값 존재 (제목 기준)")
    void search_findAllCategoryAndPageByTitle() {

        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long categoryId = category.getId();

        PostSearchDto postSearchDto = PostSearchDto.builder()
                .categoryId(categoryId)
                .searchSelect("title")
                .searchWord("test1")
                .build();

        Post post = Post.builder()
                .title("test1")
                .content("test")
                .createDate(new Date())
                .category(category)
                .member(member).build();
        postRepository.save(post);

        Post post2 = Post.builder()
                .title("test")
                .content("test")
                .createDate(new Date())
                .category(category)
                .member(member).build();
        postRepository.save(post2);

        //when
        List<PostListDto> postListDtos = postRepository.findAllByCategoryIdAndPage(postSearchDto, pageable);

        //then
        assertEquals(1, postListDtos.size());

    }


    @Test
    @DisplayName("PostListDto 리스트 검색 조회 - 값 존재 (작성자 기준)")
    void search_findAllCategoryAndPageByWriter() {

        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long categoryId = category.getId();

        PostSearchDto postSearchDto = PostSearchDto.builder()
                .categoryId(categoryId)
                .searchSelect("writer")
                .searchWord("member")
                .build();

        Post post = Post.builder()
                .title("test1")
                .content("test")
                .createDate(new Date())
                .category(category)
                .member(member).build();
        postRepository.save(post);

        Post post2 = Post.builder()
                .title("test")
                .content("test")
                .createDate(new Date())
                .category(category)
                .member(member2).build();
        postRepository.save(post2);

        //when
        List<PostListDto> postListDtos = postRepository.findAllByCategoryIdAndPage(postSearchDto, pageable);

        //then
        assertEquals(1, postListDtos.size());
    }
    
    @Test
    @DisplayName("PostListDto 리스트 조회 - 카테고리 자식요소 포함하여 조회")
    void findPostList_childCategoryByParentCategory() {
    
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long categoryId = category.getId();

        PostSearchDto postSearchDto = PostSearchDto.builder()
                .categoryId(categoryId)
                .build();

        Post post = Post.builder()
                .title("test1")
                .content("test")
                .createDate(new Date())
                .category(category)
                .member(member).build();
        postRepository.save(post);

        Post post2 = Post.builder()
                .title("test")
                .content("test")
                .createDate(new Date())
                .category(childCategory)
                .member(member2).build();
        postRepository.save(post2);
    
        //when
        List<PostListDto> postListDtos = postRepository.findAllByCategoryIdAndPage(postSearchDto, pageable);

        //then
        assertEquals(2, postListDtos.size());
    }

    @Test
    @DisplayName("PostListDto 리스트 조회 - 게시글 제목의 템플릿이 올바른지 ")
    void collectTitleTemplateByfindAllPostList() {
    
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long categoryId = category.getId();

        PostSearchDto postSearchDto = PostSearchDto.builder()
                .categoryId(categoryId)
                .build();

        Post post = Post.builder()
                .title("test1")
                .content("test")
                .createDate(new Date())
                .category(category)
                .member(member).build();
        postRepository.save(post);

        String titleTemplate = "[" + category.getName() + "] " + post.getTitle();

        //when
        List<PostListDto> list = postRepository.findAllByCategoryIdAndPage(postSearchDto, pageable);


        //then
        assertEquals(1, list.size());
        assertEquals(titleTemplate, list.get(0).getTitle());
        System.out.println(titleTemplate);
    }

}
