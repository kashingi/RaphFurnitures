package com.raph_furniture.dto;

import com.raph_furniture.model.Category;
import lombok.Data;

//Add your annotations here
@Data
public class CategoryDto {

    private Long id;

    private String name;

    private String status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
