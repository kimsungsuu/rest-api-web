package com.sungsu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sungsu.domain.SpringBoard;
import com.sungsu.repository.BoardRepository;
import com.sungsu.request.BoardEdit;
import com.sungsu.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clean(){
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청 결과 hello world")
    void test() throws Exception {
    //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

    //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                ).andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }
    @Test
    @DisplayName("글 작성 요청 시 title 값은 필수")
    void test2() throws Exception {

    //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

    //when
      mockMvc.perform(post("/posts")
              .contentType(APPLICATION_JSON)
              .content(json)
              ).andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.code").value("400"))
              .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
              .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요"))
              .andDo(print());
    }

    @Test
    @DisplayName("글 작성 요청 시 결과가 DB에 저장되는지 확인")
    void test3() throws Exception{

        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                        .content("내용입니다.")
                                .build();

        String json = objectMapper.writeValueAsString(request);
        //when
        mockMvc.perform(post("/posts")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(1L,boardRepository.count());

        SpringBoard board = boardRepository.findAll().get(0);

        assertEquals("제목입니다.",board.getTitle());
        assertEquals("내용입니다.",board.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        SpringBoard springBoard = SpringBoard.builder()
                .title("12345")
                .content("내용")
                .build();

        boardRepository.save(springBoard);

        // expected
        mockMvc.perform(get("/posts/{postId}", springBoard.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(springBoard.getId()))
                .andExpect(jsonPath("$.title").value("12345"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andDo(print());
    }

//    @Test
//    @DisplayName("글 여러개 조회")
//    void test5() throws Exception {
//        //given
//        SpringBoard springBoard = boardRepository.save(SpringBoard.builder()
//                .title("12345")
//                .content("내용")
//                .build());
//
//        SpringBoard springBoard2 = boardRepository.save(SpringBoard.builder()
//                .title("제목2")
//                .content("내용2")
//                .build());
//
//        // expected
//        mockMvc.perform(get("/posts")
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", Matchers.is(2)))
//                .andExpect(jsonPath("$[0].id").value(springBoard.getId()))
//                .andExpect(jsonPath("$[0].title").value("12345"))
//                .andExpect(jsonPath("$[0].content").value("내용"))
//                .andExpect(jsonPath("$[1].id").value(springBoard2.getId()))
//                .andExpect(jsonPath("$[1].title").value("제목2"))
//                .andExpect(jsonPath("$[1].content").value("내용2"))
//                .andDo(print());
//
//        assertEquals(2, boardRepository.findAll().size());
//        assertEquals("12345", boardRepository.findAll().get(0).getTitle());
//        assertEquals("제목2", boardRepository.findAll().get(1).getTitle());
//
//    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        List<SpringBoard> requestPosts = IntStream.range(1, 31)
                        .mapToObj(i -> SpringBoard.builder()
                                .title("제목 : " + i)
                                .content("내용 : " + i)
                                .build())
                                .collect(Collectors.toList());
        boardRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",is(5)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("제목 : 30"))
                .andExpect(jsonPath("$[0].content").value("내용 : 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 테스트")
    void test6() throws Exception {
        //given
      SpringBoard springBoard = SpringBoard.builder()
                        .title("김성수 원" )
                        .content("의정부 원")
                        .build();

      boardRepository.save(springBoard);

        BoardEdit boardEdit = BoardEdit.builder()
                .title("김성수 투")
                .content("의정부 투")
                        .build();

        // expected
        mockMvc.perform(patch("/posts/{boardId}", springBoard.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제 테스트")
    void test7() throws Exception {
        //given
        SpringBoard springBoard = SpringBoard.builder()
                .title("김성수 원" )
                .content("의정부 원")
                .build();

        boardRepository.save(springBoard);

        // expected
        mockMvc.perform(delete("/posts/{boardId}", springBoard.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 글 조회")
    void test8() throws Exception{
        mockMvc.perform(delete("/posts/{boardId}", 1)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 글 수정")
    void test9() throws Exception{


        BoardEdit boardEdit = BoardEdit.builder()
                .title("김성수 투")
                .content("의정부 투")
                .build();


        mockMvc.perform(patch("/posts/{boardId}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}