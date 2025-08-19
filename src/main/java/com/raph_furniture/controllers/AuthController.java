package com.raph_furniture.controllers;

import com.raph_furniture.ApiResponse.ApiResponse;
import com.raph_furniture.dto.LoginDto;
import com.raph_furniture.dto.UserDto;
import com.raph_furniture.model.User;
import com.raph_furniture.services.UserService;
import com.raph_furniture.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    public ResponseEntity<ApiResponse<List<UserWrapper>>> getAllUsers() {
        try {
            ResponseEntity<List<UserWrapper>> serviceResponse = userService.getAllUsers();
            List<UserWrapper> users = serviceResponse.getBody();

            ApiResponse<List<UserWrapper>> body = ApiResponse.<List<UserWrapper>>builder()
                    .statusCode(serviceResponse.getStatusCodeValue())
                    .message((users == null || users.isEmpty()) ? "No users found" : "Fetched users successfully")
                    .data(users)
                    .build();

            return ResponseEntity.status(serviceResponse.getStatusCode()).body(body);
        } catch (Exception ex) {
            // log the error if you have a logger
            ApiResponse<List<UserWrapper>> body = ApiResponse.<List<UserWrapper>>builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to fetch users")
                    .data(Collections.emptyList())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }
}


