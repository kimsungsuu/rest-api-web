package com.sungsu.controller;

import com.sungsu.request.BoardEdit;
import com.sungsu.request.PostCreate;
import com.sungsu.response.PostResponse;
import com.sungsu.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * request Dto와 entity class의 차이점
 * 1. request Dto인 PostController는 client의 요청 데이터를 관리하고
 * 2. entity 클래스는 DB에 데이터를 최종적으로 저장하거나, DB에 있는 데이터를 가져오는 역할을 한다.
 * 3. Dto 클래스는 주로 요청을 처리하기 때문에 Controller에서 주로 쓰인다.
 * 4. entity 클래스는 DB의 데이터를 다루기 떄문에 Service 클래스에서 주로 쓰인다.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final BoardService boardService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {

        boardService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Integer postId){
        return boardService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return boardService.getList(pageable);
    }

    @PatchMapping("/posts/{boardId}")
    public void edit(@PathVariable Integer boardId, @RequestBody @Valid BoardEdit boardEdit){
        boardService.edit(boardId, boardEdit);
    }

}
