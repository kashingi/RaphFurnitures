package com.raph_furniture.dto;

import com.raph_furniture.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

//Add your annotations here
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    private String name;

    private Double discount;

    private Boolean status;

    private String categoryImage;
}
