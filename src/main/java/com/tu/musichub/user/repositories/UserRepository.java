package com.tu.musichub.user.repositories;

import com.tu.musichub.user.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    User findByUsername(String username);

    Page<User> findAllByUsernameContains(String username, Pageable pageable);

    @Query(value = "SELECT CASE WHEN (SELECT COUNT(u) FROM User AS u " +
            "INNER JOIN u.authorities AS r WHERE " +
            "u.username = :username AND r.authority IN :roleNames) > 0 " +
            "THEN true ELSE false END FROM User")
    boolean isUserHasAnyRole(@Param("username") String username, @Param("roleNames") String... roleNames);
}
