package com.userMicroservice.UserMicroservice.repositories;

import com.userMicroservice.UserMicroservice.models.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokensRepository extends JpaRepository<Tokens, Long> {
}
