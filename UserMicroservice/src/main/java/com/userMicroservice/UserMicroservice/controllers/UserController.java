package com.userMicroservice.UserMicroservice.controllers;

import com.userMicroservice.UserMicroservice.dto.*;
import com.userMicroservice.UserMicroservice.models.User;
import com.userMicroservice.UserMicroservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResDTO>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        List<UserResDTO> usersRes = users.stream().map(UserResDTO::new).toList();
        return new ResponseEntity<>(usersRes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResDTO> getOneUser(@PathVariable long id){
        User user = userService.findUserById(id);
        return new ResponseEntity<>(new UserResDTO(user), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<UserResDTO> registration(@Valid @RequestBody UserReqDTO dto){
        User user = userService.createUser(dto);
        return new ResponseEntity<>(new UserResDTO(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResDTO> updateUser(@Valid @RequestBody UserUpdateDTO dto, @PathVariable long id){
        User updatedUser = userService.updateUser(dto, id);
        return new ResponseEntity<>(new UserResDTO(updatedUser), HttpStatus.OK);
    }

    @PatchMapping("/update-password/{id}")
    public ResponseEntity<UserResDTO> updatePasswordOfUser(@Valid @RequestBody UpdatePasswordOfUserDTO dto, @PathVariable long id){
        User updatedUser = userService.updatePasswordOfUser(dto, id);
        return new ResponseEntity<>(new UserResDTO(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(long id){
        userService.deleteUserById(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    @PatchMapping("/ban/{id}")
    public ResponseEntity<String> banUser(@PathVariable long id){
        userService.banUser(id);
        return new ResponseEntity<>("User successfully banned", HttpStatus.OK);
    }

    @PatchMapping("/unban/{id}")
    public ResponseEntity<String> unbanUser(@PathVariable long id){
        userService.unbanUser(id);
        return new ResponseEntity<>("User successfully unbanned", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto){
        return new ResponseEntity<>("Successfully login", HttpStatus.OK);
    }
}
