package com.raph_furniture.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Add your annotations here
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWrapper {

    private Integer id;

    private String name;

    private String email;

    private String contact;

    private String role;

//    public UserWrapper(Integer id, String name, String email, String contact, String role) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.contact = contact;
//        this.role = role;
//    }


}
