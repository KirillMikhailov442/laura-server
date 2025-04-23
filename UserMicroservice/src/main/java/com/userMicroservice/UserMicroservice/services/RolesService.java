package com.userMicroservice.UserMicroservice.services;

import com.userMicroservice.UserMicroservice.exceptions.Conflict;
import com.userMicroservice.UserMicroservice.exceptions.NotFound;
import com.userMicroservice.UserMicroservice.interfaces.UserRoles;
import com.userMicroservice.UserMicroservice.models.Roles;
import com.userMicroservice.UserMicroservice.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RolesService {
    @Autowired
    private RolesRepository rolesRepository;

    public Roles findRoleById(long id){
        return rolesRepository.findById(id)
                .orElseThrow(()-> new NotFound("Role not found by id"));
    }

    public Roles findRoleByName(UserRoles name){
        return rolesRepository.findByName(name)
                .orElseThrow(()-> new NotFound("Role not found by name"));
    }

    public Roles createRole(UserRoles name){
        Optional<Roles> role = rolesRepository.findByName(name);
        if(role.isPresent()){
            return role.get();
        }
        Roles newRole = Roles.builder().name(name).build();
        return rolesRepository.save(newRole);
    }

    public Roles updateNameOfRole(UserRoles oldName, UserRoles newName){
        Roles role = findRoleByName(oldName);
        Roles otherRole = findRoleByName(newName);

        if(Optional.of(otherRole).isPresent()) throw new Conflict("role \"user\" already exists in the database");

        role.setName(newName);
        return rolesRepository.save(role);
    }
}
