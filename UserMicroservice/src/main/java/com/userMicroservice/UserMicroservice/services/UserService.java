package com.userMicroservice.UserMicroservice.services;

import com.userMicroservice.UserMicroservice.dto.*;
import com.userMicroservice.UserMicroservice.exceptions.BadRequest;
import com.userMicroservice.UserMicroservice.exceptions.Conflict;
import com.userMicroservice.UserMicroservice.exceptions.NotFound;
import com.userMicroservice.UserMicroservice.interfaces.UserRoles;
import com.userMicroservice.UserMicroservice.models.Roles;
import com.userMicroservice.UserMicroservice.models.User;
import com.userMicroservice.UserMicroservice.repositories.UserRepository;
import com.userMicroservice.UserMicroservice.utils.GeneratorKeys;
import com.userMicroservice.UserMicroservice.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private GeneratorKeys generatorKeys;
    @Autowired
    private JWTUtils jwtUtils;

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) throw new NotFound("Users not found");
        return users;
    }

    public User findUserById(long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new NotFound("User not found by id"));
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFound("User not found by email"));
    }

    public User findUserByNickname(String nickname){
        return userRepository.findByNickname(nickname)
                .orElseThrow(()-> new NotFound("User not found by nickname"));
    }

    public void confirmUser(long id){
        User user = findUserById(id);
        user.setConfirmed(true);
        userRepository.save(user);
    }

    public User createUser(UserReqDTO dto){
        if(userRepository.existsByEmail(dto.getEmail())) throw new Conflict("A user with such email already exists");
        if(userRepository.existsByNickname(dto.getNickname())) throw new Conflict("A user with such nickname already exists");

        Set<Roles> roles = Set.of(rolesService.createRole(UserRoles.USER));
        User newUser = User.builder().firstName(dto.getFirstName()).lastName(dto.getLastName())
                .email(dto.getEmail()).nickname(dto.getNickname())
                .password(encoder.encode(dto.getPassword()))
                .deactivatedAt(null).isConfirmed(false).roles(roles)
                .build();
        User createdUser = userRepository.save(newUser);
        System.out.println(generatorKeys.encode(String.valueOf(createdUser.getId())));
        return createdUser;
    }

    public User updateUser(UserUpdateDTO dto, long id){
        User user = findUserById(id);
        if(userRepository.existsByEmail(dto.getEmail()) && user.getId() != id){
            throw new Conflict("A user with such email already exists");
        }
        if(userRepository.existsByNickname(dto.getNickname()) && user.getId() != id){
            throw new Conflict("A user with such nickname already exists");
        }
        if(dto.getFirstName() != null && !dto.getFirstName().isEmpty()) user.setFirstName(dto.getFirstName());
        if(dto.getLastName() != null && !dto.getLastName().isEmpty()) user.setLastName(dto.getLastName());
        if(dto.getEmail() != null && !dto.getEmail().isEmpty()) user.setEmail(dto.getEmail());
        if(dto.getNickname() != null && !dto.getNickname().isEmpty()) user.setNickname(dto.getNickname());

        return userRepository.save(user);
    }

    public User updatePasswordOfUser(UpdatePasswordOfUserDTO dto, long id){
        User user = findUserById(id);
        if(!encoder.encode(dto.getOldPassword()).equals(user.getPassword())) throw new Conflict("Old password does not match user password");
        user.setPassword(encoder.encode(dto.getNewPassword()));
        return userRepository.save(user);
    }

    public void deleteUserById(long id){
        userRepository.deleteById(id);
    }

    public void banUser(long id){
        User user = findUserById(id);
        user.setDeactivatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void unbanUser(long id){
        User user = findUserById(id);
        user.setDeactivatedAt(null);
        userRepository.save(user);
    }

    public TokensDTO login(LoginDTO dto){
        User user = findUserByEmail(dto.getEmail());
        System.out.println(user.getPassword());
        System.out.println(encoder.encode(dto.getPassword()));
        System.out.println(encoder.matches(dto.getPassword(), user.getPassword()));
        if(user.getPassword() != encoder.encode(dto.getPassword())) throw new BadRequest("Incorrect password");

        String accessToken = jwtUtils.generateAccessToken(new UserPrincipal(user), null);
        return TokensDTO.builder().accessToken(accessToken).build();
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(username))
                .orElseThrow(()-> new UsernameNotFoundException(username));
        return new UserPrincipal(user);
    }
}
