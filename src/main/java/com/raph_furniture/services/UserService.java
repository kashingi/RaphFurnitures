package com.raph_furniture.services;

import com.raph_furniture.dto.LoginDto;
import com.raph_furniture.dto.UserDto;
import com.raph_furniture.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

//Add your annotations here
public interface UserService {
    ResponseEntity<String> register(UserDto userDto);

    ResponseEntity<String> login(LoginDto loginDto);

    ResponseEntity<List<User>> getAllUsers();
}
