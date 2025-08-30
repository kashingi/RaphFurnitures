package com.raph_furniture.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

//Add your annotations here
@Data
@NoArgsConstructor

public class UserDto {

    private String name;

    private String email;

    private String contact;

    private String role;

    private String password;



    public UserDto(String name, String email, String contact, String role, String password) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.role = role;
        this.password = password;
    }
}
