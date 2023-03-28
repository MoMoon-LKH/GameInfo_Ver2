package com.gmi.gameInfo.category.service;

import com.gmi.gameInfo.category.domain.Category;
import com.gmi.gameInfo.category.domain.dto.CategoryDto;
import com.gmi.gameInfo.category.exception.NotFoundCategoryException;
import com.gmi.gameInfo.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;


    @Transactional
    public Category save(CategoryDto categoryDto) {
        return categoryRepository.save(new Category(categoryDto));
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(NotFoundCategoryException::new);
        categoryRepository.delete(category);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(NotFoundCategoryException::new);
    }
}
