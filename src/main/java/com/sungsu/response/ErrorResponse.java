package com.sungsu.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

//@RequiredArgsConstructor
@Getter
//@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;

    private final Map<String, String> validation = new HashMap<>();


    @Builder
    public ErrorResponse(String code, String message){
        this.code = code;
        this.message = message;
    }

    public void addValidation(String fieldName, String errorMessage){
        this.validation.put(fieldName, errorMessage);
    }
}
