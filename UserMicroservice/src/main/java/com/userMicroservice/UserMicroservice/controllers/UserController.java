package com.userMicroservice.UserMicroservice.controllers;

import com.userMicroservice.UserMicroservice.dto.*;
import com.userMicroservice.UserMicroservice.models.User;
import com.userMicroservice.UserMicroservice.services.UserService;
import com.userMicroservice.UserMicroservice.utils.GeneratorKeys;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    @Autowired
    private GeneratorKeys generatorKeys;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getOneUser(@PathVariable long id){
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<User> registration(@Valid @RequestBody UserReqDTO dto){
        User user = userService.createUser(dto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserUpdateDTO dto, @PathVariable long id){
        User updatedUser = userService.updateUser(dto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PatchMapping("/update-password/{id}")
    public ResponseEntity<User> updatePasswordOfUser(@Valid @RequestBody UpdatePasswordOfUserDTO dto, @PathVariable long id){
        User updatedUser = userService.updatePasswordOfUser(dto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

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
        TokensDTO tokens = userService.login(dto);
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

}
