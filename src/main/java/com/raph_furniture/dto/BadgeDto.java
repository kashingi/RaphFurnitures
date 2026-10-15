package com.raph_furniture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

//Add your annotations here
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadgeDto {

    private Long id;

    private String title;

    private String description;

    private String badgeName;

    private MultipartFile image;
}
