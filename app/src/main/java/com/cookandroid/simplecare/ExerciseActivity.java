package com.cookandroid.simplecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    // UI 요소 선언
    private EditText searchEditText;
    private Button searchButton;
    private TextView totalCaloriesTextView;
    private ListView exerciseListView;

    private Button saveButton;
    private Button backButton;
    private Button resetButton;
    private Button calButton;

    // 데이터 관련 변수 선언
    private List<Exercise> exerciseList;
    private ArrayAdapter<String> adapter;
    private List<String> searchResults;
    private int selectedExerciseCalories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_input);

        // UI 요소 초기화
        searchEditText = findViewById(R.id.search_edittext);
        searchButton = findViewById(R.id.search_button);
        totalCaloriesTextView = findViewById(R.id.total_calories_textview);
        exerciseListView = findViewById(R.id.exercise_listview);

        saveButton = findViewById(R.id.save_button);
        backButton = findViewById(R.id.back_button);
        resetButton = findViewById(R.id.reset_button);
        calButton = findViewById(R.id.cal_button);

        // 운동 목록 초기화
        exerciseList = new ArrayList<>();
        exerciseList.add(new Exercise("팔굽혀펴기", 100));
        exerciseList.add(new Exercise("스쿼트", 150));
        exerciseList.add(new Exercise("런닝머신", 200));
        exerciseList.add(new Exercise("플랭크", 120));
        exerciseList.add(new Exercise("덤벨 숄더 프레스", 180));
        exerciseList.add(new Exercise("크런치", 90));
        exerciseList.add(new Exercise("바벨 벤치 프레스", 180));
        exerciseList.add(new Exercise("레그 프레스", 160));
        exerciseList.add(new Exercise("체어 딥스", 130));
        exerciseList.add(new Exercise("랫 풀 다운", 140));
        exerciseList.add(new Exercise("바벨 로우", 160));
        exerciseList.add(new Exercise("레그 컬", 70));
        exerciseList.add(new Exercise("플라잉 피셔맨", 90));

        // 어댑터 초기화
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        exerciseListView.setAdapter(adapter);

        searchResults = new ArrayList<>();

        // 검색 버튼 클릭 리스너 설정
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exerciseName = searchEditText.getText().toString().trim();
                searchExerciseAndUpdateList(exerciseName);
            }
        });

        // 저장 버튼 클릭 리스너 설정
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedExerciseCalories > 0) {
                    totalCaloriesTextView.setText("선택한 운동의 칼로리: " + selectedExerciseCalories + "kcal");

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("exerciseCalories", selectedExerciseCalories);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(ExerciseActivity.this, "운동을 선택하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 뒤로 버튼 클릭 리스너 설정
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 리셋 버튼 클릭 리스너 설정
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedExerciseCalories = 0;
                totalCaloriesTextView.setText("선택한 운동의 칼로리: ");
            }
        });

        // 유튜브 웹 검색 버튼 클릭 리스너 설정
        calButton.setOnClickListener(v -> {
            Intent intent = new Intent(ExerciseActivity.this, ExerciseRecordActivity.class);
            startActivity(intent);
        });

        // 운동 목록 항목 클릭 리스너 설정
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedExerciseName = searchResults.get(position);
                selectedExerciseCalories = searchExercise(selectedExerciseName);
                totalCaloriesTextView.setText("선택한 운동의 칼로리: " + selectedExerciseCalories + "kcal");
            }
        });
    }

    // 운동 검색 및 칼로리 반환 메서드
    private int searchExercise(String exerciseName) {
        int calories = 0;
        for (Exercise exercise : exerciseList) {
            if (exercise.getName().equalsIgnoreCase(exerciseName)) {
                calories = exercise.getCalories();
                break;
            }
        }
        return calories;
    }

    // 운동 검색 및 리스트 업데이트 메서드
    private void searchExerciseAndUpdateList(String exerciseName) {
        searchResults.clear();
        for (Exercise exercise : exerciseList) {
            if (exercise.getName().toLowerCase().contains(exerciseName.toLowerCase())) {
                searchResults.add(exercise.getName());
            }
        }
        adapter.clear();
        adapter.addAll(searchResults);
        adapter.notifyDataSetChanged();
    }
}
