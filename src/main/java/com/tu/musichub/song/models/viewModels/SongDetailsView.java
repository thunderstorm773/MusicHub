package com.tu.musichub.song.models.viewModels;

import com.tu.musichub.song.comment.models.viewModels.CommentView;
import com.tu.musichub.song.tag.models.viewModels.TagView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class SongDetailsView {

    private Long id;

    private String title;

    private String categoryName;

    private String uploaderUsername;

    private String streamingUrl;

    private String downloadUrl;

    private Date uploadedOn;

    private Set<TagView> tags;

    private List<CommentView> comments;
}
