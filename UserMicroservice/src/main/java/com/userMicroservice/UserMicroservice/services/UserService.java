package com.userMicroservice.UserMicroservice.services;

import com.userMicroservice.UserMicroservice.dto.UserDTO;
import com.userMicroservice.UserMicroservice.exceptions.Conflict;
import com.userMicroservice.UserMicroservice.exceptions.NotFound;
import com.userMicroservice.UserMicroservice.interfaces.UserRoles;
import com.userMicroservice.UserMicroservice.models.Roles;
import com.userMicroservice.UserMicroservice.models.User;
import com.userMicroservice.UserMicroservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    PasswordEncoder encoder = new BCryptPasswordEncoder();


    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) throw new NotFound("Users not found");
        return users;
    }

    public User findUserById(long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new NotFound("User not found by id"));
        return user;
    }

    public User findUserByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFound("User not found by email"));
        return user;
    }

    public User findUserByNickname(String nickname){
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(()-> new NotFound("User not found by nickname"));
        return user;
    }

    public void confirmUser(long id){
        User user = findUserById(id);
        user.setConfirmed(true);
        userRepository.save(user);
    }

    public User createUser(UserDTO dto){
        if(userRepository.existsByEmail(dto.getEmail())) throw new Conflict("A user with such email already exists");
        if(userRepository.existsByNickname(dto.getNickname())) throw new Conflict("A user with such nickname already exists");

        User newUser = User.builder().firstName(dto.getFirstName()).lastName(dto.getLastName())
                .email(dto.getEmail()).nickname(dto.getNickname())
                .password(encoder.encode(dto.getPassword()))
                .deactivatedAt(null).isConfirmed(false).roles(Set.of(Roles.builder().name(UserRoles.USER).build()))
                .build();
        return userRepository.save(newUser);
    }

    public User updateUser(UserDTO dto, long id){
        User user = findUserById(id);
        if(!userRepository.existsByEmail(dto.getEmail())){
            throw new Conflict("A user with such email already exists");
        }
        if(!userRepository.existsByNickname(dto.getNickname())){
            throw new Conflict("A user with such nickname already exists");
        }
        if(dto.getFirstName() != null && !Objects.equals(user.getFirstName(), dto.getFirstName())) user.setFirstName(dto.getFirstName());
        if(dto.getLastName() != null && !Objects.equals(user.getLastName(), dto.getLastName())) user.setLastName(dto.getLastName());
        if(dto.getPassword() != null && !Objects.equals(user.getPassword(), encoder.encode(dto.getPassword()))) user.setPassword(encoder.encode(dto.getPassword()));
        if(dto.getEmail() != null && !Objects.equals(user.getEmail(), dto.getEmail())) user.setEmail(dto.getEmail());
        if(dto.getNickname() != null && !Objects.equals(user.getNickname(), dto.getNickname())) user.setNickname(dto.getNickname());

        return userRepository.save(user);
    }

    public void banUser(long id){
        User user = findUserById(id);
        user.setDeactivatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void debanUser(long id){
        User user = findUserById(id);
        user.setDeactivatedAt(null);
        userRepository.save(user);
    }

}
