package com.sungsu.response;

import com.sungsu.domain.SpringBoard;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

    private final Integer id;
    private final String title;
    private final String content;

    //생성자 오버로딩
    public PostResponse(SpringBoard springBoard){
        String title = springBoard.getTitle();
        this.id = springBoard.getId();
        this.title = title.substring(0, Math.min(springBoard.getTitle().length(), 10));
        this.content = springBoard.getContent();
    }

    @Builder
    public PostResponse(Integer id, String title, String content) {
        this.id = id;
        this.title = title.substring(0,Math.min(title.length(), 10));
        this.content = content;
    }
}
