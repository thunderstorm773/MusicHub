package com.softuni.musichub.song.tag.models.bindingModels;

public class AddTag {

    private String name;

    public AddTag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
