package com.tu.musichub.user.repositories;

import com.tu.musichub.user.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

    PasswordResetToken findByTokenAndExpiryDateAfter(String token, Date date);
}
