package com.userMicroservice.UserMicroservice.services;

import com.userMicroservice.UserMicroservice.interfaces.UserRoles;
import com.userMicroservice.UserMicroservice.models.Roles;
import com.userMicroservice.UserMicroservice.models.User;
import com.userMicroservice.UserMicroservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AdminService {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesService rolesService;

    public User upgradeToAdmin(long id){
        User user = userService.findUserById(id);
        Roles roleAdmin = rolesService.createRole(UserRoles.ADMIN);
        Set<Roles> roles = user.getRoles();
        roles.add(roleAdmin);
        user.setRoles(roles);

        return userRepository.save(user);
    }
}
