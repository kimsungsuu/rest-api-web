package com.sungsu.service;

import com.sungsu.domain.SpringBoard;

public class HelpTest {

    public SpringBoard createBoard(){
        SpringBoard springBoard = SpringBoard.builder()
                .title("제목")
                .content("내용")
                .build();

        return springBoard;
    }
}
