package com.domgarr.UI_Challenge.models;

import java.util.List;

public class Category {
    private String name;
    private List<Song> list;

    public Category(String name, List<Song> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getList() {
        return list;
    }

    public void setList(List<Song> list) {
        this.list = list;
    }
}
