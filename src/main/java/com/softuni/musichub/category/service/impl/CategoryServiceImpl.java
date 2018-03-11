package com.softuni.musichub.category.service.impl;

import com.softuni.musichub.category.entity.Category;
import com.softuni.musichub.category.exception.CategoryNotFoundException;
import com.softuni.musichub.category.model.bindingModel.AddCategory;
import com.softuni.musichub.category.model.bindingModel.EditCategory;
import com.softuni.musichub.category.model.view.CategoryView;
import com.softuni.musichub.category.repository.CategoryRepository;
import com.softuni.musichub.category.service.api.CategoryService;
import com.softuni.musichub.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final MapperUtil mapperUtil;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               MapperUtil mapperUtil) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public void addCategory(AddCategory addCategory) {
        Category category = this.mapperUtil.getModelMapper()
                .map(addCategory, Category.class);
        this.categoryRepository.save(category);
    }

    @Override
    public CategoryView findByName(String categoryName)
            throws CategoryNotFoundException {
        Category category = this.categoryRepository.findByName(categoryName);
        if (category == null) {
            throw new CategoryNotFoundException();
        }

        CategoryView categoryView = this.mapperUtil.getModelMapper()
                .map(category, CategoryView.class);
        return categoryView;
    }

    @Override
    public List<CategoryView> findAll() {
        List<Category> categories = this.categoryRepository.findAll();
        List<CategoryView> categoryViews = this.mapperUtil
                .convertAll(categories, CategoryView.class);
        return categoryViews;
    }

    @Override
    public CategoryView findById(Long categoryId) throws CategoryNotFoundException {
        Optional<Category> optionalCategory = this.categoryRepository
                .findById(categoryId);
        if (!optionalCategory.isPresent()) {
            throw new CategoryNotFoundException();
        }

        Category category = optionalCategory.get();
        CategoryView categoryView = this.mapperUtil.getModelMapper()
                .map(category, CategoryView.class);
        return categoryView;
    }

    @Override
    public void deleteById(Long categoryId) throws CategoryNotFoundException {
        boolean isCategoryExists = this.categoryRepository.existsById(categoryId);
        if (!isCategoryExists) {
            throw new CategoryNotFoundException();
        }

        this.categoryRepository.deleteById(categoryId);
    }

    @Override
    public void edit(EditCategory editCategory, Long id) throws CategoryNotFoundException {
        boolean isCategoryExists = this.categoryRepository.existsById(id);
        if (!isCategoryExists) {
            throw new CategoryNotFoundException();
        }

        Category category = this.mapperUtil.getModelMapper()
                .map(editCategory, Category.class);
        category.setId(id);
        this.categoryRepository.save(category);
    }
}