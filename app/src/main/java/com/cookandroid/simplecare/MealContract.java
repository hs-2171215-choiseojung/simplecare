package com.cookandroid.simplecare;

import android.provider.BaseColumns;

public final class MealContract {

    private MealContract() {} // 빈 생성자

    public static class MealEntry implements BaseColumns {
        public static final String TABLE_NAME = "meals";
        public static final String COLUMN_DESC_KOR = "descKor";
        public static final String COLUMN_SERVING_WT = "servingWt";
        public static final String COLUMN_NUTR_CONT1 = "nutrCont1";
        public static final String COLUMN_NUTR_CONT2 = "nutrCont2";
        public static final String COLUMN_NUTR_CONT3 = "nutrCont3";
        public static final String COLUMN_NUTR_CONT4 = "nutrCont4";
        public static final String COLUMN_NUTR_CONT5 = "nutrCont5";
        public static final String COLUMN_NUTR_CONT6 = "nutrCont6";
    }
}
