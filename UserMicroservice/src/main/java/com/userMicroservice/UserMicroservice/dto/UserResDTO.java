package com.userMicroservice.UserMicroservice.dto;

import com.userMicroservice.UserMicroservice.interfaces.UserRoles;
import com.userMicroservice.UserMicroservice.models.Roles;
import com.userMicroservice.UserMicroservice.models.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class UserResDTO{
    final private long id;
    final private String firstName;
    final private String lastName;
    final private String email;
    final private String password;
    final private String nickname;
    final private Set<UserRoles> roles;
    final private LocalDateTime createdAt;
    final private LocalDateTime updatedAt;
    final private LocalDateTime deactivatedAt;

    public UserResDTO(User user) {;
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.roles = user.getRoles().stream().map(Roles::getName).collect(Collectors.toSet());
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.deactivatedAt = user.getDeactivatedAt();
    }
}
