package com.raph_furniture.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Add your annotations here
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadgeWrapper {

    private Long id;

    private String title;

    private String description;

    private String badgeName;

    private byte[] image;
}
