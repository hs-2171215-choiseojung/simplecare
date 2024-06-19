package com.cookandroid.simplecare;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.graphics.Color;

public class p1 extends AppCompatActivity {

    private List<String> weightList = new ArrayList<>();

    private static final String TAG = "p1";
    private static final int WEIGHT_INPUT_REQUEST = 1;
    private static final int MEAL_INPUT_REQUEST = 2; // 새로운 request code 추가

    private static final int EXERCISE_INPUT_REQUEST = 3;
    private Button weightButton;
    private Button mealinputbutton;
    private Button exerciserecordbutton;
    private EditText currentWeightEditText;
    private EditText goalWeightEditText;
    private EditText heightEditText;
    private EditText ageEditText;

    private int exerciseCalories = 0; // 운동 칼로리 값

    private LineChart lineChart;

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1);

        weightButton = findViewById(R.id.weight_button);
        mealinputbutton = findViewById(R.id.meal_input_button);
        exerciserecordbutton = findViewById(R.id.exercise_record_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        lineChart = findViewById(R.id.chart); // 그래프 뷰를 XML 레이아웃과 연결

        setUpButtonListeners();

        setupLineChart(); // 그래프 설정
        loadChartData();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(this, p1.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_food_log) {
                startActivity(new Intent(this,ExerciseDietTrackerActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                startActivity(new Intent(this, MyPageActivity.class));
                return true;
            }
            return false;
        });



    }





    private void setUpButtonListeners() {
        findViewById(R.id.meal_input_button).setOnClickListener(this::onButtonClicked);
        findViewById(R.id.weight_button).setOnClickListener(this::onButtonClicked);
        findViewById(R.id.temporary_button).setOnClickListener(this::onButtonClicked);
        findViewById(R.id.exercise_record_button).setOnClickListener(this::onButtonClicked);
        findViewById(R.id.exercise_diary_button).setOnClickListener(this::onButtonClicked);
    }
    private void onButtonClicked(View view) {
        int id = view.getId();

        if (id == R.id.meal_input_button) {
            // 식단 입력 화면으로 이동
            Toast.makeText(p1.this, "식단 입력 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Meal Input Button clicked");
            Intent intent = new Intent(p1.this, MealInputActivity.class);
            startActivityForResult(intent, MEAL_INPUT_REQUEST); // request code 변경
        } else if (id == R.id.weight_button) {
            // 체중 입력 화면으로 이동
            Toast.makeText(p1.this, "체중 입력 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Weight Button clicked");
            Intent intent = new Intent(p1.this, WeightInputActivity.class);
            startActivityForResult(intent, WEIGHT_INPUT_REQUEST);
        } else if (id == R.id.temporary_button) {
            // 간헐적 타이머 화면으로 이동
            Toast.makeText(p1.this, "간헐적 타이머 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Temporary Button clicked");
            startActivity(new Intent(p1.this, IntermittentFastingActivity.class));

        } else if (view.getId() == R.id.exercise_record_button) {
            // 운동 기록 화면으로 이동
            Toast.makeText(p1.this, "운동 기록 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Exercise Record Button clicked");
            Intent intent = new Intent(p1.this, ExerciseActivity.class);
            startActivityForResult(intent, EXERCISE_INPUT_REQUEST); // 수정된 부분

        } else if (id == R.id.exercise_diary_button) {
            // 운동 일지 화면으로 이동
            Toast.makeText(p1.this, "일지 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Exercise Diary Button clicked");
            startActivity(new Intent(p1.this, ExerciseDietTrackerActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WEIGHT_INPUT_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                String weight = data.getStringExtra("weight");
                String time = data.getStringExtra("time");
                String weightLeft = data.getStringExtra("weight_left");
                weightButton.setText(String.format(Locale.getDefault(), "체중 입력: %skg\n%s\n남은 목표 체중: %s", weight, time, weightLeft));
                addWeightToChart(weight);
            }
        } else if (requestCode == MEAL_INPUT_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                double totalCalories = data.getDoubleExtra("totalCalories", 0); // 데이터 타입을 double로 수정
                mealinputbutton.setText(String.format(Locale.getDefault(), "식단 입력\n총 섭취 칼로리: %.1f kcal", totalCalories)); // 정수에서 소수점까지 출력할 수 있도록 형식 수정

            }
        } else if (requestCode == EXERCISE_INPUT_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                int exerciseCalories = data.getIntExtra("exerciseCalories", 0);
                exerciserecordbutton.setText(String.format(Locale.getDefault(), "운동 기록\n소모 칼로리: %d kcal", exerciseCalories));
            }

        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 그래프 데이터 저장
        outState.putStringArrayList("weightList", new ArrayList<>(weightList));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // 그래프 데이터 복원
        if (savedInstanceState.containsKey("weightList")) {
            weightList = savedInstanceState.getStringArrayList("weightList");
            loadChartData(); // 복원된 데이터로 그래프 다시 그리기
        }
    }
    private void setupLineChart() {
        // Chart 설정
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(true);
        lineChart.setBackgroundColor(0xFFFFFFFF); // Background color

        // X축 설정
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);

        // Y축 설정
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setLabelCount(6, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Legend 설정
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
    }



    private void loadChartData() {
        List<Entry> values = new ArrayList<>();

        // 새로운 체중 데이터 추가
        for (int i = 0; i < weightList.size(); i++) {
            values.add(new Entry(i, Float.parseFloat(weightList.get(i))));
        }

        LineDataSet set1 = new LineDataSet(values, "체중");
        set1.setColor(Color.RED);
        set1.setLineWidth(2f);
        set1.setCircleColor(Color.RED);
        set1.setCircleRadius(5f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(10f);
        set1.setDrawFilled(false);
        set1.setFillColor(Color.RED);

        LineData data = new LineData(set1);
        lineChart.setData(data);
        lineChart.invalidate(); // 그래프 업데이트
    }


    // 그래프에 체중 값을 추가하는 메서드
    private void addWeightToChart(String weight) {
        LineData lineData = lineChart.getData();
        LineDataSet lineDataSet = null;

        if (lineData != null && lineData.getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) lineData.getDataSetByIndex(0);
            int xIndex = lineData.getEntryCount(); // 현재 데이터 개수를 x값으로 설정
            float yValue = Float.parseFloat(weight); // 체중 값을 실수형으로 변환하여 y값으로 설정
            lineDataSet.addEntry(new Entry(xIndex, yValue));
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.invalidate(); // 그래프 업데이트
        } else {
            // 초기 데이터가 없을 경우 처리
        }
    }
    // 체중을 리스트에 추가하는 메서드
    private void addWeightToList(String weight) {
        weightList.add(weight);
    }
    }
