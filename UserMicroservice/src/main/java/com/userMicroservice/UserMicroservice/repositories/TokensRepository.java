package com.userMicroservice.UserMicroservice.repositories;

import com.userMicroservice.UserMicroservice.models.Tokens;
import com.userMicroservice.UserMicroservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokensRepository extends JpaRepository<Tokens, Long> {
    public List<Tokens> findAllByUserIdId(long userId);
    public Optional<Tokens> findByRefreshToken(String refresh);
}
