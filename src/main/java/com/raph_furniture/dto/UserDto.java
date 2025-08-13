package com.raph_furniture.dto;

import lombok.Data;

//Add your annotations here
@Data
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
