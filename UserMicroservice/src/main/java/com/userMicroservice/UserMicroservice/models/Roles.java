package com.userMicroservice.UserMicroservice.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.userMicroservice.UserMicroservice.interfaces.UserRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
 @Builder
 @AllArgsConstructor
 @NoArgsConstructor
 @Entity
 @Table(name = "roles")
 public class Roles {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;

     @Enumerated(EnumType.STRING)
     @Column(name = "name", length = 15, unique = true)
     private UserRoles name;

//    @ManyToMany(mappedBy = "roles")
//    private List<User> users;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
 }
