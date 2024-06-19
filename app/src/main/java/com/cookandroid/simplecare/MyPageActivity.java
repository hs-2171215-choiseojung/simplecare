package com.cookandroid.simplecare;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class MyPageActivity extends AppCompatActivity {

    private EditText goalWeightEditText;
    private EditText heightEditText;
    private EditText currentWeightEditText;
    private EditText ageEditText;
    private RadioGroup genderRadioGroup;
    private Button saveButton;
    private Button backButton;

    public static final String PREFS_NAME = "MyPagePrefs";
    private static final String KEY_GOAL_WEIGHT = "goal_weight";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_CURRENT_WEIGHT = "current_weight";
    private static final String KEY_AGE = "age";
    public static final String KEY_GENDER = "gender";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        goalWeightEditText = findViewById(R.id.goal_weight_edit_text);
        heightEditText = findViewById(R.id.height_edit_text);
        currentWeightEditText = findViewById(R.id.current_weight_edit_text);
        ageEditText = findViewById(R.id.age_edit_text);
        genderRadioGroup = findViewById(R.id.gender_radio_group);
        saveButton = findViewById(R.id.save_button);
        backButton = findViewById(R.id.back_button); // 뒤로가기 버튼 연결

        loadUserInfo(); // 사용자 정보 불러오기

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // 뒤로가기 버튼 클릭 시 동작
            }
        });
    }


    private void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String goalWeight = sharedPreferences.getString(KEY_GOAL_WEIGHT, "");
        String height = sharedPreferences.getString(KEY_HEIGHT, "");
        String currentWeight = sharedPreferences.getString(KEY_CURRENT_WEIGHT, "");
        String age = sharedPreferences.getString(KEY_AGE, "");
        String gender = sharedPreferences.getString(KEY_GENDER, "");

        goalWeightEditText.setText(goalWeight);
        heightEditText.setText(height);
        currentWeightEditText.setText(currentWeight);
        ageEditText.setText(age);
        if (gender.equals("male")) {
            genderRadioGroup.check(R.id.male_radio_button);
        } else if (gender.equals("female")) {
            genderRadioGroup.check(R.id.female_radio_button);
        }
    }

    // 성별 정보를 저장하는 메서드
    private void saveUserInfo() {
        String goalWeight = goalWeightEditText.getText().toString();
        String height = heightEditText.getText().toString();
        String currentWeight = currentWeightEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String gender;
        int checkedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.male_radio_button) {
            gender = "male";
        } else if (checkedRadioButtonId == R.id.female_radio_button) {
            gender = "female";
        } else {
            gender = "";
        }

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_GOAL_WEIGHT, goalWeight);
        editor.putString(KEY_HEIGHT, height);
        editor.putString(KEY_CURRENT_WEIGHT, currentWeight);
        editor.putString(KEY_AGE, age);
        editor.putString(KEY_GENDER, gender);
        editor.apply();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 뒤로가기 버튼을 눌렀을 때 P1으로 이동
    }
}
