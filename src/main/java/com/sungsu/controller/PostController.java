package com.sungsu.controller;

import com.sungsu.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class PostController {

    @PostMapping("/posts")
    public String post(@RequestBody PostCreate params) {
        log.info("params={}",params.toString());

        return "hello World";
    }
}
