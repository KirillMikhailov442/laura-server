package com.userMicroservice.UserMicroservice.controllers;

import com.userMicroservice.UserMicroservice.dto.*;
import com.userMicroservice.UserMicroservice.models.User;
import com.userMicroservice.UserMicroservice.services.UserService;
import com.userMicroservice.UserMicroservice.utils.GeneratorKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "User", description = "Official user API")
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private GeneratorKeys generatorKeys;

    @Operation(summary = "Get all users", description = "This endpoint allows you to get all users from the database")
    @SecurityRequirement(name = "JWT")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @Operation(summary = "Get one user", description = "This endpoint allows you to get one user with id")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    public ResponseEntity<User> getOneUser(@PathVariable long id){
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }

    @Operation(summary = "Create new account", description = "This endpoint allows you to create a new account, by default you are given the role of \"User\"")
    @PostMapping
    public ResponseEntity<User> registration(@Valid @RequestBody UserReqDTO dto){
        User user = userService.createUser(dto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Update user data", description = "This endpoint allows you to update your account information other than your password")
    @SecurityRequirement(name = "JWT")
    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserUpdateDTO dto){
        long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User updatedUser = userService.updateUser(dto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(summary = "Refresh user password", description = "This endpoint allows you to update the password of the user")
    @SecurityRequirement(name = "JWT")
    @PatchMapping("/update-password")
    public ResponseEntity<User> updatePasswordOfUser(@Valid @RequestBody UpdatePasswordOfUserDTO dto){
        long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User updatedUser = userService.updatePasswordOfUser(dto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(summary = "Verify your account", description = "This app allows you to verify your account")
    @PatchMapping("/confirm/{code}")
    public ResponseEntity<String> confirmUser(@Valid @PathVariable @NotNull(message = "Code must not be null") String code){
       try{
           long userId = Long.parseLong(generatorKeys.decode(code));
           String firstName = userService.findUserById(userId).getFirstName();
           userService.confirmUser(userId);
           return new ResponseEntity<>(String.format("User %s has been successfully verified",firstName), HttpStatus.OK);
       }catch (Exception ex){
           return new ResponseEntity<>("Failed to verify user", HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @Operation(summary = "Delete account", description = "This endpoint allows you to delete your account")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping
    public ResponseEntity<String> deleteUser(){
        long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        userService.deleteUserById(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    @Operation(summary = "Account login", description = "The endpoint allows authorization, followed by a pair of tokens")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto){
        TokensDTO tokens = userService.login(dto);
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @Operation(summary = "Update tokens", description = "This endpoint allows you to update the access token and the refresh token")
    @PatchMapping("/update-tokens")
    public ResponseEntity<TokensDTO> updateRefreshToken(@Valid @RequestBody TokensCreateDTO dto){
        return new ResponseEntity<>(userService.updateRefreshTokenOfUser(dto), HttpStatus.CREATED);
    }
}
