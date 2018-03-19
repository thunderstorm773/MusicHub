package com.softuni.musichub.user.repository;

import com.softuni.musichub.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    User findByUsername(String username);

    Page<User> findAllByUsernameContains(String username, Pageable pageable);
}
