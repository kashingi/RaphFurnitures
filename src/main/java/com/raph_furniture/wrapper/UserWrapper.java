package com.raph_furniture.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

//Add your annotations here
@Data
@NoArgsConstructor
public class UserWrapper {

    private Integer id;

    private String name;

    private String email;

    private String contact;

    private String role;

    public UserWrapper(Integer id, String name, String email, String contact, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
