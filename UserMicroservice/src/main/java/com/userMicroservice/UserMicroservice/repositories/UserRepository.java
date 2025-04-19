package com.userMicroservice.UserMicroservice.repositories;

import com.userMicroservice.UserMicroservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByNickname(String nickname);
    public boolean existsByEmail(String email);
    public boolean existsByNickname(String nickname);
}
