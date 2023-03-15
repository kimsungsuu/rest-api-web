package com.sungsu.service;


import com.sungsu.domain.SpringBoard;
import com.sungsu.repository.BoardRepository;
import com.sungsu.request.BoardEdit;
import com.sungsu.request.PostCreate;
import com.sungsu.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.domain.Sort.Direction.DESC;

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

    @Test
    @DisplayName("글 1page 조회")
    void getList(){
        //given
        List<SpringBoard> requestPosts = IntStream.range(1, 31)
                        .mapToObj(i -> SpringBoard.builder()
                                    .title("제목 : " + i)
                                    .content("의정부 - " + i)
                                    .build())
                .collect(Collectors.toList());
        boardRepository.saveAll(requestPosts);

        Pageable pageable = PageRequest.of(0, 5, Sort.by(DESC, "id"));

        //when
        List<PostResponse> list = boardService.getList(pageable);


        //then
        assertEquals(5, list.size());
        assertEquals("제목 : 30", list.get(0).getTitle());
        assertEquals("제목 : 26", list.get(4).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정 조회")
    void getEdit(){

        //given
       SpringBoard springBoard = SpringBoard.builder()
                        .title("김성수 원")
                        .content("의정부 원")
                        .build();

       boardRepository.save(springBoard);

       BoardEdit boardEdit = BoardEdit.builder()
                       .title("김성수 투")
                                       .build();

        //when
        boardService.edit(springBoard.getId(), boardEdit);

        //then
        SpringBoard changeBoard = boardRepository.findById(springBoard.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id = "+  springBoard.getId()));

        assertEquals("김성수 투", changeBoard.getTitle());

    }
}
