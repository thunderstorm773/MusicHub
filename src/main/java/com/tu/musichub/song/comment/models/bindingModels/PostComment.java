package com.tu.musichub.song.comment.models.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostComment {

    private String content;

    private Long songId;

    public PostComment(String content, Long songId) {
        this.content = content;
        this.songId = songId;
    }
}
