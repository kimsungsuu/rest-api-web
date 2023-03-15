package com.sungsu.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SpringBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Lob
    private String content;

    @Builder
    public SpringBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void edit(BoardEditor boardEditor) {
        title = boardEditor.getTitle();
        content = boardEditor.getContent();

    }

    public BoardEditor.BoardEditorBuilder toEditor(){
        return BoardEditor.builder()
                .title(title)
                .content(content);
    }
}
