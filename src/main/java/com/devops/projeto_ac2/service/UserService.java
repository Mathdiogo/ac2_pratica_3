package com.devops.projeto_ac2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devops.projeto_ac2.dto.UserDTO;
import com.devops.projeto_ac2.entity.User;
import com.devops.projeto_ac2.repository.User_Repository;

@Service
public class UserService {

    @Autowired
    private User_Repository user_repository;

    public List<UserDTO> getAllUsers(){
        List<User> users = user_repository.findAll();
        return users.stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }


}
