package com.sungsu.service;

import com.sungsu.domain.SpringBoard;
import com.sungsu.repository.BoardRepository;
import com.sungsu.request.PostCreate;
import com.sungsu.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;


    public void write(PostCreate postCreate){

        SpringBoard springBoard = SpringBoard.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

         boardRepository.save(springBoard);
    }

    public PostResponse get(Integer id){
//        SpringBoard springBoard = boardRepository.findById(id); //Optional로 반환해줘야 하기 때문에 오류 발생.
        SpringBoard springBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return new PostResponse(springBoard);

//       return PostResponse.builder()
//                .id(springBoard.getId())
//                .title(springBoard.getTitle())
//                .content(springBoard.getContent())
//                .build();
    }

    public List<PostResponse> getList() {
                return boardRepository.findAll().stream()
                .map(springBoard -> new PostResponse(springBoard))
                        .collect(Collectors.toList());

    }
}
