package com.sungsu.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BoardEdit {

    @NotBlank(message = "타이틀을 입력하세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력하세요.")
    private String content;

    @Builder
    public BoardEdit(String title, String content){
        this.title = title;
        this.content = content;
    }
}
