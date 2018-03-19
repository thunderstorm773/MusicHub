package com.softuni.musichub.category.repository;

import com.softuni.musichub.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    Category findByName(String name);
}
