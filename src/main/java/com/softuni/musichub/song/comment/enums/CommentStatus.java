package com.softuni.musichub.song.comment.enums;

public enum CommentStatus {

    PENDING("Waiting for review"), APPROVED("Approved"), REJECTED("Rejected");

    String statusValue;

    CommentStatus(String statusValue) {
        this.statusValue = statusValue;
    }

    @Override
    public String toString() {
        return this.statusValue;
    }
}
