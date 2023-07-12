package com.gmi.gameInfo.category.service;


import com.gmi.gameInfo.category.domain.Category;
import com.gmi.gameInfo.category.domain.dto.CategoryDto;
import com.gmi.gameInfo.category.exception.NotFoundCategoryException;
import com.gmi.gameInfo.category.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;


    @Test
    @DisplayName("카테고리 저장")
    void saveCategory() {

        //given
        CategoryDto categoryDto = CategoryDto
                .builder()
                .name("test")
                .build();
        Category category = new Category(categoryDto);
        given(categoryRepository.save(any(Category.class))).willReturn(category);

        //when
        Category save = categoryService.save(categoryDto);

        //then
        assertEquals(categoryDto.getName(), save.getName());
    }
    
    @Test
    @DisplayName("카테고리 조회 - 실패")
    void findByIdFail() {
    
        //given
        given(categoryRepository.findById(any())).willReturn(Optional.ofNullable(null));

        //when

        //then
        assertThrows(NotFoundCategoryException.class, () -> {
            categoryService.findById(1L);
        });
    }
    
    @Test
    @DisplayName("카테고리 조회 - 성공")
    void findByIdSuccess() {
    
        //given
        Category category = Category.builder()
                .id(1L)
                .name("test")
                .build();
        given(categoryRepository.findById(any(Long.class))).willReturn(Optional.of(category));

        //when
        Category find = categoryService.findById(1L);

        //then
        assertSame(category, find);
        assertEquals(category.getName(), category.getName());
    }
    
    @Test
    @DisplayName("카테고리 삭제 - 실패")
    void deleteCategoryFail() {
    
        //given
        given(categoryRepository.findById(any())).willReturn(Optional.ofNullable(null));

    
        //when
        
        //then
        assertThrows(NotFoundCategoryException.class, () -> {
            categoryService.delete(1L);
        });
    }
    
    @Test
    @DisplayName("카테고리 삭제 - 성공")
    void deleteCategorySuccess() {
    
        //given
        Category category = Category.builder()
                .id(1L)
                .name("test")
                .build();
        given(categoryRepository.findById(any())).willReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(any(Category.class));
    
        //when
        categoryService.delete(category.getId());

        given(categoryRepository.findById(any())).willReturn(Optional.ofNullable(null));

        //then
        assertThrows(NotFoundCategoryException.class, () -> {
            categoryService.findById(category.getId());
        });
    }
}
