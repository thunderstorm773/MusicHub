package com.softuni.musichub.admin.category.services;

import com.softuni.musichub.admin.category.entities.Category;
import com.softuni.musichub.admin.category.models.views.CategoryView;
import com.softuni.musichub.admin.category.repositories.CategoryRepository;
import com.softuni.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryExtractionServiceImpl implements CategoryExtractionService {

    private final CategoryRepository categoryRepository;

    private final MapperUtil mapperUtil;

    @Autowired
    public CategoryExtractionServiceImpl(CategoryRepository categoryRepository,
                                         MapperUtil mapperUtil) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CategoryView findByName(String categoryName) {
        Category category = this.categoryRepository.findByName(categoryName);
        if (category == null) {
            return null;
        }

        return this.mapperUtil.getModelMapper().map(category, CategoryView.class);
    }

    @Override
    public Page<CategoryView> findAll(Pageable pageable) {
        Page<Category> categoryPage = this.categoryRepository.findAll(pageable);
        return this.mapperUtil.convertToPage(pageable, categoryPage, CategoryView.class);
    }

    @Override
    public List<CategoryView> findAll() {
        Iterable<Category> categories = this.categoryRepository.findAll();
        return this.mapperUtil.convertAll(categories, CategoryView.class);
    }

    @Override
    public CategoryView findById(Long categoryId) {
        Category category = this.categoryRepository
                .findById(categoryId).orElse(null);
        if (category == null) {
            return null;
        }

        return this.mapperUtil.getModelMapper().map(category, CategoryView.class);
    }
}
