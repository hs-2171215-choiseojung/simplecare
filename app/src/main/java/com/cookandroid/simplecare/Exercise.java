package com.cookandroid.simplecare;

public class Exercise {
    private String name;
    private int calories;

    public Exercise(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }
}
