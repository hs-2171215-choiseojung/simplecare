package com.cookandroid.simplecare;

public class TodoItem {
    private String title;
    private String description;

    public TodoItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

