package com.raph_furniture.controllers;

import com.raph_furniture.dto.LoginDto;
import com.raph_furniture.dto.UserDto;
import com.raph_furniture.services.UserService;
import com.raph_furniture.wrapper.UserWrapper;
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
    public ResponseEntity<List<UserWrapper>>getAllUsers() {
        return userService.getAllUsers();
    }

    //Implement updates user details here
    @PutMapping(path = "/updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    //Implement update user status here
    @PutMapping(path = "/updateRole/{id}")
    public ResponseEntity<String>updateStatus(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.updateRole(id, userDto);
    }

    //Implement delete function here
    @DeleteMapping(path = "/deleteUser/{id}")
    public ResponseEntity<String>deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
