package com.cookandroid.simplecare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeightInputActivity extends AppCompatActivity {

    private EditText weightInput;
    private EditText heightInput;
    private EditText goalWeightInput;
    private TextView bmiResult;
    private TextView bmiCategory;
    private TextView weightLeftResult;
    private Spinner unitSpinner;
    private Button saveButton;
    private Button backButton;

    private static final String PREFS_NAME = "MyPagePrefs";
    private static final String KEY_GOAL_WEIGHT = "goal_weight";
    private static final String KEY_HEIGHT = "height";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_input);

        weightInput = findViewById(R.id.weight_input);
        heightInput = findViewById(R.id.height_input);
        goalWeightInput = findViewById(R.id.goal_weight_input);
        bmiResult = findViewById(R.id.bmi_result);
        bmiCategory = findViewById(R.id.bmi_category);
        weightLeftResult = findViewById(R.id.weight_left_result);
        unitSpinner = findViewById(R.id.unit_spinner);
        saveButton = findViewById(R.id.save_button);
        backButton = findViewById(R.id.back_button);

        float goalWeight = getIntent().getFloatExtra("goal_weight", 0.0f);
        float height = getIntent().getFloatExtra("height", 0.0f);

        goalWeightInput.setText(String.valueOf(goalWeight));
        heightInput.setText(String.valueOf(height));

        loadUserData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndDisplayBMI();
                calculateAndDisplayWeightLeft();
                saveGoalWeight();
                saveData();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    private void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String goalWeight = sharedPreferences.getString(KEY_GOAL_WEIGHT, "");
        String height = sharedPreferences.getString(KEY_HEIGHT, "");

        goalWeightInput.setText(goalWeight);
        heightInput.setText(height);
    }

    private void calculateAndDisplayBMI() {
        String weightStr = weightInput.getText().toString();
        String heightStr = heightInput.getText().toString();
        String unit = unitSpinner.getSelectedItem().toString();

        if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr);

            if (unit.equals("lbs/inch")) {
                weight = convertLbsToKg(weight);
                height = convertInchesToMeters(height);
            } else {
                height /= 100; // cm to meters
            }

            float bmi = weight / (height * height);
            bmiResult.setText(String.format(Locale.getDefault(), "BMI: %.2f", bmi));
            displayBMICategory(bmi);
        }
    }

    private void displayBMICategory(float bmi) {
        String category;
        if (bmi < 18.5) {
            category = "저체중";
        } else if (bmi < 24.9) {
            category = "정상체중";
        } else if (bmi < 29.9) {
            category = "과체중";
        } else {
            category = "비만";
        }
        bmiCategory.setText(String.format(Locale.getDefault(), "BMI 범주: %s", category));
    }

    private void calculateAndDisplayWeightLeft() {
        String weightStr = weightInput.getText().toString();
        String goalWeightStr = goalWeightInput.getText().toString();

        if (!weightStr.isEmpty() && !goalWeightStr.isEmpty()) {
            float weight = Float.parseFloat(weightStr);
            float goalWeight = Float.parseFloat(goalWeightStr);

            float weightLeft = goalWeight - weight;
            weightLeftResult.setText(String.format(Locale.getDefault(), "남은 체중: %.2fkg", weightLeft));
        }
    }

    private void saveGoalWeight() {
        String goalWeight = goalWeightInput.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_GOAL_WEIGHT, goalWeight);
        editor.apply();
    }

    private void saveData() {
        String weight = weightInput.getText().toString();
        String currentTime = getCurrentTime();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("weight", weight);
        resultIntent.putExtra("time", currentTime);

        // 남은 목표 체중 계산
        float goalWeight = Float.parseFloat(goalWeightInput.getText().toString());
        float currentWeight = Float.parseFloat(weight);
        float weightLeft = goalWeight - currentWeight;
        resultIntent.putExtra("weight_left", String.format(Locale.getDefault(), "%.2fkg", weightLeft));

        setResult(RESULT_OK, resultIntent);
    }

    private void goBack() {
        finish();  // Simply close the activity without sending any data back
    }

    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private float convertLbsToKg(float lbs) {
        return lbs * 0.45359237f;
    }

    private float convertInchesToMeters(float inches) {
        return inches * 0.0254f; // 1 inch = 0.0254 meters
    }
}
