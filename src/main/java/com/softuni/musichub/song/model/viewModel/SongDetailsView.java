package com.softuni.musichub.song.model.viewModel;

import com.softuni.musichub.tag.model.viewModel.TagView;
import java.util.Date;
import java.util.Set;

public class SongDetailsView {

    private Long id;

    private String title;

    private String categoryName;

    private String uploaderUsername;

    private String streamingUrl;

    private String downloadUrl;

    private Date uploadedOn;

    private Set<TagView> tags;

    public SongDetailsView() {
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUploaderUsername() {
        return uploaderUsername;
    }

    public void setUploaderUsername(String uploaderUsername) {
        this.uploaderUsername = uploaderUsername;
    }

    public String getStreamingUrl() {
        return streamingUrl;
    }

    public void setStreamingUrl(String streamingUrl) {
        this.streamingUrl = streamingUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Date getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(Date uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public Set<TagView> getTags() {
        return tags;
    }

    public void setTags(Set<TagView> tags) {
        this.tags = tags;
    }
}
