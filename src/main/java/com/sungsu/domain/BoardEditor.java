package com.sungsu.domain;


import lombok.Getter;

@Getter
public class BoardEditor {

    private String title;
    private String content;


    public BoardEditor(String title, String content){
        this.title = title;
        this.content = content;
    }

    public static BoardEditorBuilder builder() {
        return new BoardEditorBuilder();
    }

    public static class BoardEditorBuilder {
        private String title;
        private String content;

        BoardEditorBuilder() {
        }

        public BoardEditorBuilder title(final String title) {
            if(title != null){
                this.title = title;
            }
            return this;
        }

        public BoardEditorBuilder content(final String content) {
            if(content != null){
                this.content = content;
            }
            return this;
        }

        public BoardEditor build() {
            return new BoardEditor(this.title, this.content);
        }

        public String toString() {
            return "BoardEditor.BoardEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
        }
    }
}
