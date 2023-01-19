package com.sparta.ojinger.service;

import com.sparta.ojinger.entity.Category;
import com.sparta.ojinger.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> getCategoryFromString(String s) {
        String[] categories = s.split(",");
        List<Category> categoryList = new ArrayList<>();
        for (String singleCategory : categories) {
            Optional<Category> optionalCategory = categoryRepository.findByCategoryName(singleCategory.trim());
            Category category;
            if (optionalCategory.isEmpty()) {
                category = categoryRepository.save(new Category(singleCategory.trim()));
            } else {
                category = optionalCategory.get();
            }
            categoryList.add(category);
        }

        return categoryList;
    }
}
