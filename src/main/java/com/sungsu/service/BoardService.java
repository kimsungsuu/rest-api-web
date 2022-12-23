package com.sungsu.service;

import com.sungsu.domain.SpringBoard;
import com.sungsu.repository.BoardRepository;
import com.sungsu.request.PostCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public void write(PostCreate postCreate){

        SpringBoard springBoard = new SpringBoard(postCreate.getTitle(), postCreate.getContent());

        boardRepository.save(springBoard);
    }


}
