package com.gmi.gameInfo.category.repository;


import com.gmi.gameInfo.category.domain.Category;
import com.gmi.gameInfo.category.exception.NotFoundCategoryException;
import com.gmi.gameInfo.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
public class CategoryRepositoryTest {


    @Autowired
    private CategoryRepository categoryRepository;

    private String name = "test";


    @Test
    @Rollback
    @DisplayName("카테고리 저장")
    void saveCategory() {

        //given
        Category category = new Category(name, 0L);

        //when
        Category save = categoryRepository.save(category);

        //then
        assertEquals(name, save.getName());
    }

    @Test
    @DisplayName("카테고리 조회 실패")
    void findByCategoryIdFail() {

        //given

        //when

        //then
        assertThrows(NotFoundCategoryException.class, () -> {
            categoryRepository.findById(0L).orElseThrow(NotFoundCategoryException::new);
        });
    }

    @Test
    @Rollback
    @DisplayName("카테고리 조회 성공")
    void findByCategoryId() {

        //given
        Category category = saveCategory(name);

        //when
        Category find = categoryRepository.findById(category.getId()).orElseThrow(NotFoundCategoryException::new);

        //then
        assertEquals(name, find.getName());
    }


    Category saveCategory(String name) {
        Category category = new Category(name, 0L);

        return categoryRepository.save(category);
    }

    @Test
    @Rollback
    @DisplayName("카테고리 삭제")
    void deleteCategory() {

        //given
        Category category = saveCategory(name);

        //when
        categoryRepository.delete(category);

        //then
        assertThrows(NotFoundCategoryException.class, () -> {
            categoryRepository.findById(category.getId()).orElseThrow(NotFoundCategoryException::new);
        });
    }

}
