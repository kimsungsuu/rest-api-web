package com.sungsu.service;


import com.sungsu.domain.SpringBoard;
import com.sungsu.repository.BoardRepository;
import com.sungsu.request.PostCreate;
import com.sungsu.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void clean(){
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void save(){
        // given
        PostCreate request = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        // when
        boardService.write(request);

        // then
        assertEquals(1, boardRepository.count());
        SpringBoard springBoard = boardRepository.findAll().get(0);
        assertEquals("제목", springBoard.getTitle());
        assertEquals("내용", springBoard.getContent());
    }

    @Test
    @DisplayName("글 조회")
    void get(){
        //given
        SpringBoard requestBoard = SpringBoard.builder()
                .title("제목")
                .content("내용")
                .build();

        boardRepository.save(requestBoard);

        //when
        PostResponse postResponse = boardService.get(requestBoard.getId());


        //then
        assertNotNull(postResponse);
        assertEquals(1, boardRepository.count());
        assertEquals("제목", postResponse.getTitle());
        assertEquals("내용", postResponse.getContent());
    }

}
