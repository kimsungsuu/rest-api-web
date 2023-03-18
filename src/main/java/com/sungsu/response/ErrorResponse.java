package com.sungsu.response;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


/**
 * {
 *     "code" : "400",
 *     "message" : "잘못된 요청입니다.""
 *     "validation" : { // 어던 field가 error 인지 명시해주기 위함.
 *         "title" : "값을 입력해주세요"
 *     }
 * }
 */
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
