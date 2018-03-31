package com.softuni.musichub.song.models.viewModels;

import com.softuni.musichub.song.tag.models.viewModels.TagView;
import java.util.Set;

public class SongView {

    private Long id;

    private String title;

    private String uploaderUsername;

    private String categoryName;

    private Set<TagView> tags;

    public SongView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploaderUsername() {
        return uploaderUsername;
    }

    public void setUploaderUsername(String uploaderUsername) {
        this.uploaderUsername = uploaderUsername;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<TagView> getTags() {
        return tags;
    }

    public void setTags(Set<TagView> tags) {
        this.tags = tags;
    }
}
