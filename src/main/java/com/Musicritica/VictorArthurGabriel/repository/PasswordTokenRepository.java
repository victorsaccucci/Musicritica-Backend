package com.Musicritica.VictorArthurGabriel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Musicritica.VictorArthurGabriel.entity.PasswordResetToken;


public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    PasswordResetToken findByToken(String token);

}
