package com.cookandroid.simplecare;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class SharedPreferencesHelper {
    private static final String SHARED_PREF_NAME = "meal_preferences";
    private static final String KEY_SELECTED_MEALS = "selected_meals";
    private static final String KEY_ACCUMULATED_CALORIES = "accumulated_calories";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveSelectedMeals(List<MealItem> selectedMeals) {
        String json = gson.toJson(selectedMeals);
        sharedPreferences.edit().putString(KEY_SELECTED_MEALS, json).apply();
    }

    public List<MealItem> loadSelectedMeals() {
        String json = sharedPreferences.getString(KEY_SELECTED_MEALS, "");
        Type type = new TypeToken<List<MealItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveAccumulatedCalories(double accumulatedCalories) {
        sharedPreferences.edit().putFloat(KEY_ACCUMULATED_CALORIES, (float) accumulatedCalories).apply();
    }

    public double loadAccumulatedCalories() {
        return sharedPreferences.getFloat(KEY_ACCUMULATED_CALORIES, 0);
    }
}

