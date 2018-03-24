package com.softuni.musichub.user.repository;

import com.softuni.musichub.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    User findByUsername(String username);

    Page<User> findAllByUsernameContains(String username, Pageable pageable);

    @Query(value = "SELECT IF((SELECT COUNT(*) FROM users AS u INNER JOIN users_roles AS ur " +
            "ON u.id = ur.user_id " +
            "INNER JOIN roles AS r " +
            "ON ur.role_id = r.id " +
            "WHERE u.username = :username AND r.name IN :roleNames), true, false)", nativeQuery = true)
    Object isUserHasAnyRole(@Param("username") String username, @Param("roleNames") String... roleNames);
}
