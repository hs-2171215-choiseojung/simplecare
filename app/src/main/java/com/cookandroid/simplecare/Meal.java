package com.cookandroid.simplecare;

public class Meal {
    private int id;
    private String name;
    private String description;

    public Meal(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getter 메서드들
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
