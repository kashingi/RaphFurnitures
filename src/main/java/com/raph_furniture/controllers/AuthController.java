package com.raph_furniture.controllers;

import com.raph_furniture.dto.LoginDto;
import com.raph_furniture.dto.UserDto;
import com.raph_furniture.model.User;
import com.raph_furniture.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Add your imports here
@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    //Autowire what you will need here
    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        return userService.register(userDto);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

    //get all users
    @GetMapping(path = "/getAllUsers")
    public ResponseEntity<List<User>>getAllUsers() {
        return userService.getAllUsers();
    }
}
