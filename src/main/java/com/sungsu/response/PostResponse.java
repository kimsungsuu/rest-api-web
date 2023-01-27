package com.sungsu.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

    private final Integer id;
    private final String title;
    private final String content;

    @Builder
    public PostResponse(Integer id, String title, String content) {
        this.id = id;
        this.title = title.substring(0,Math.min(title.length(), 10));
        this.content = content;
    }
}
