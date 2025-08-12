package com.raph_furniture.ApiResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Standard API response format")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    @Schema(description = "HTTP status code", example = "200")
    private int statusCode;

    @Schema(description = "Response message", example = "Operation successful")
    private String message;

    @Schema(description = "Response data payload")
    private T data;
}
