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

}
