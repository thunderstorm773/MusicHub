package com.softuni.musichub.song.comment.models.bindingModels;

public class PostComment {

    private String content;

    private Long songId;

    public PostComment() {
    }

    public PostComment(String content, Long songId) {
        this.content = content;
        this.songId = songId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }
}
