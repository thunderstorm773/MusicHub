package com.softuni.musichub.admin.category.repositories;

import com.softuni.musichub.admin.category.entities.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    Category findByName(String name);
}
