package com.sungsu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sungsu.domain.SpringBoard;
import com.sungsu.repository.BoardRepository;
import com.sungsu.request.PostCreate;
import com.sungsu.service.BoardService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ).andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());
    }
    @Test
    @DisplayName("/posts title 값은 필수")
    void test2() throws Exception {

    //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

    //when
      mockMvc.perform(post("/posts")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              ).andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.code").value("400"))
              .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
              .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요"))
              .andDo(print());
    }

    @Test
    @DisplayName("/posts 결과가 DB에 저장되는지 확인")
    void test3() throws Exception{

        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                        .content("내용입니다.")
                                .build();

        String json = objectMapper.writeValueAsString(request);
        //when
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(1L,boardRepository.count());

        SpringBoard board = boardRepository.findAll().get(0);

        assertEquals("제목입니다.",board.getTitle());
        assertEquals("내용입니다.",board.getContent());
    }

}