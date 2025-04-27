package com.userMicroservice.UserMicroservice.controllers;

import com.userMicroservice.UserMicroservice.dto.UpdatePasswordOfUserDTO;
import com.userMicroservice.UserMicroservice.dto.UserUpdateDTO;
import com.userMicroservice.UserMicroservice.models.User;
import com.userMicroservice.UserMicroservice.services.AdminService;
import com.userMicroservice.UserMicroservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin", description = "Official admin API")
@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Operation(summary = "Get one user", description = "This endpoint allows you to get one user with id")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserUpdateDTO dto, @PathVariable long id){
        User updatedUser = userService.updateUser(dto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(summary = "Refresh user password", description = "This endpoint allows you to update the password of the user with id")
    @PatchMapping("/update-password/{id}")
    public ResponseEntity<User> updatePasswordOfUser(@Valid @RequestBody UpdatePasswordOfUserDTO dto, @PathVariable long id){
        User updatedUser = userService.updatePasswordOfUser(dto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(summary = "Delete account", description = "This endpoint allows you to delete your account")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(long id){
        userService.deleteUserById(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    @Operation(summary = "Block a user", description = "This endpoint allows you to block the user account using id")
    @PatchMapping("/ban/{id}")
    public ResponseEntity<String> banUser(@PathVariable long id){
        userService.banUser(id);
        return new ResponseEntity<>("User successfully banned", HttpStatus.OK);
    }

    @Operation(summary = "Unlock user", description = "This endpoint allows you to unlock the user account using id")
    @PatchMapping("/unban/{id}")
    public ResponseEntity<String> unbanUser(@PathVariable long id){
        userService.unbanUser(id);
        return new ResponseEntity<>("User successfully unbanned", HttpStatus.OK);
    }

    @Operation(summary = "Upgrade user to administrator", description = "This endpoint allows you to add the \"ADMIN\" role for the user id")
    @PatchMapping("/upgrade-to-admin/{id}")
    public ResponseEntity<User> upgradeToAdmin(@PathVariable long id){
        User user = adminService.upgradeToAdmin(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
