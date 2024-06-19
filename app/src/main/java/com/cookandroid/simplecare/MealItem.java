package com.cookandroid.simplecare;

public class MealItem {
    private String descKor;
    private String servingWt;
    private String nutrCont1;
    private String nutrCont2;
    private String nutrCont3;
    private String nutrCont4;
    private String nutrCont5;
    private String nutrCont6;
    private boolean selected;

    // 기본 생성자
    public MealItem() {
        this.selected = false; // 기본적으로 선택되지 않도록 설정
    }

    // 모든 필드를 포함한 생성자
    public MealItem(String descKor, String servingWt, String nutrCont1, String nutrCont2, String nutrCont3, String nutrCont4, String nutrCont5) {
        this.descKor = descKor;
        this.servingWt = servingWt;
        this.nutrCont1 = nutrCont1;
        this.nutrCont2 = nutrCont2;
        this.nutrCont3 = nutrCont3;
        this.nutrCont4 = nutrCont4;
        this.nutrCont5 = nutrCont5;
        this.nutrCont6 = nutrCont6; // 또는 원하는 기본값 설정
        this.selected = false; // 기본적으로 선택되지 않도록 설정
    }


    // 각 필드의 getter/setter 메서드
    public String getDescKor() {
        return descKor;
    }

    public void setDescKor(String descKor) {
        this.descKor = descKor;
    }

    public String getServingWt() {
        return servingWt;
    }

    public void setServingWt(String servingWt) {
        this.servingWt = servingWt;
    }

    public String getNutrCont1() {
        return nutrCont1;
    }

    public void setNutrCont1(String nutrCont1) {
        this.nutrCont1 = nutrCont1;
    }

    public String getNutrCont2() {
        return nutrCont2;
    }

    public void setNutrCont2(String nutrCont2) {
        this.nutrCont2 = nutrCont2;
    }

    public String getNutrCont3() {
        return nutrCont3;
    }

    public void setNutrCont3(String nutrCont3) {
        this.nutrCont3 = nutrCont3;
    }

    public String getNutrCont4() {
        return nutrCont4;
    }

    public void setNutrCont4(String nutrCont4) {
        this.nutrCont4 = nutrCont4;
    }

    public String getNutrCont5() {
        return nutrCont5;
    }

    public void setNutrCont5(String nutrCont5) {
        this.nutrCont5 = nutrCont5;
    }

    public String getNutrCont6() {
        return nutrCont6;
    }

    public void setNutrCont6(String nutrCont6) {
        this.nutrCont6 = nutrCont6;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}