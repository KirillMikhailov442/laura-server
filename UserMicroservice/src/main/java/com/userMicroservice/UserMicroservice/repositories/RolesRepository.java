package com.userMicroservice.UserMicroservice.repositories;

import com.userMicroservice.UserMicroservice.interfaces.UserRoles;
import com.userMicroservice.UserMicroservice.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    public Optional<Roles> findByName(UserRoles name);
    public boolean existsByName(UserRoles name);
}
