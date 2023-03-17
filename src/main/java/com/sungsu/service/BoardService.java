package com.sungsu.service;

import com.sungsu.domain.BoardEditor;
import com.sungsu.domain.SpringBoard;
import com.sungsu.repository.BoardRepository;
import com.sungsu.request.BoardEdit;
import com.sungsu.request.PostCreate;
import com.sungsu.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

//        return new PostResponse(springBoard);

       return PostResponse.builder()
                .id(springBoard.getId())
                .title(springBoard.getTitle())
                .content(springBoard.getContent())
                .build();
    }


    /**
     * 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
     * 글이 -> 100,000,000 -> DB 글 모두 조회하는 경우 -> DB가 뻗을 수 있다.
     * DB -> 애플리케이션 서버로 전달하는 시간, 트래픽 비용 등이 많이 발생할 수 있다.
     */

    public List<PostResponse> getList(Pageable pageable) {

                return boardRepository.findAll(pageable).stream()
                .map(PostResponse::new)
                        .collect(Collectors.toList());

    }

    @Transactional
    public void edit(Integer id, BoardEdit boardEdit){
        SpringBoard springBoard = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        BoardEditor.BoardEditorBuilder boardEditorBuilder = springBoard.toEditor();

        BoardEditor boardEditor = boardEditorBuilder.title(boardEdit.getTitle())
                .content(boardEdit.getContent())
                .build();

        springBoard.edit(boardEditor);
    }

    public void delete(Integer id){

        SpringBoard springBoard = boardRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        boardRepository.delete(springBoard);
    }


}
