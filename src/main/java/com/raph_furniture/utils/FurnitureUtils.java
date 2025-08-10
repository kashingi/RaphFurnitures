package com.raph_furniture.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//Add your imports here
public class FurnitureUtils {

    //Private constructor to prevent instantiation
    private FurnitureUtils() {}

    //Have your return message here
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>("{\"Message\":\"" + responseMessage + "\"}", httpStatus);
    }
}
