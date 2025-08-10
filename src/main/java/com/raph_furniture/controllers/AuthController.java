package com.raph_furniture.controllers;

import com.raph_furniture.ApiResponse.ApiResponse;
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
    public ResponseEntity<ApiResponse<String>> register(@RequestBody UserDto userDto) {
        ResponseEntity<String> serviceResponse = userService.register(userDto);
        ApiResponse<String> body = ApiResponse.<String>builder()
                .statusCode(serviceResponse.getStatusCodeValue())
                .message(serviceResponse.getBody())
                .data(null)
                .build();
        return ResponseEntity.status(serviceResponse.getStatusCode()).body(body);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginDto loginDto) {
        ResponseEntity<String> serviceResponse = userService.login(loginDto);
        ApiResponse<String> body = ApiResponse.<String>builder()
                .statusCode(serviceResponse.getStatusCodeValue())
                .message(serviceResponse.getBody())
                .data(null)
                .build();
        return ResponseEntity.status(serviceResponse.getStatusCode()).body(body);
    }

    // get all users
    @GetMapping(path = "/getAllUsers")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        ResponseEntity<List<User>> serviceResponse = userService.getAllUsers();
        ApiResponse<List<User>> body = ApiResponse.<List<User>>builder()
                .statusCode(serviceResponse.getStatusCodeValue())
                .message("Fetched users successfully")
                .data(serviceResponse.getBody())
                .build();
        return ResponseEntity.status(serviceResponse.getStatusCode()).body(body);
    }
}
