package com.cookandroid.simplecare;
import android.content.Context;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MealInputActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private Button detailButton;
    private RecyclerView recyclerView;
    private TextView detailInfoTextView;
    private MealAdapter mealAdapter;

    private Button saveButton;
    private Button resetButton;
    private Button backButton;

    private TextView totalCaloriesTextView, caloriestextview;

    private PieChart nutrientChart; // 파이 차트 변수 추가
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_input);

        saveButton = findViewById(R.id.save_button);
        backButton = findViewById(R.id.back_button);
        resetButton = findViewById(R.id.reset_button);


        searchEditText = findViewById(R.id.search_edittext);
        searchButton = findViewById(R.id.search_button);
        detailButton = findViewById(R.id.detail_button);
        recyclerView = findViewById(R.id.recycler_view);
        detailInfoTextView = findViewById(R.id.detail_info_textview);

        nutrientChart = findViewById(R.id.nutrient_chart); // 파이 차트 참조 설정


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mealAdapter = new MealAdapter(new ArrayList<>());
        recyclerView.setAdapter(mealAdapter);


        // "총 섭취 칼로리"를 보여주는 TextView 찾기
        totalCaloriesTextView = findViewById(R.id.total_calories_textview);
        caloriestextview = findViewById(R.id.calories_textview); // 여기에서 선언된 caloriestextview를 초기화


        // 저장 버튼 클릭 리스너 설정
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택된 음식들의 총 섭취 칼로리 계산 및 업데이트
                double totalCalories = calculateTotalCalories();
                double accumulatedCalories = getAccumulatedCalories() + totalCalories;
                saveAccumulatedCalories(accumulatedCalories);

                // 총 섭취 칼로리 업데이트
                updateTotalCalories();

                // 파이 차트 업데이트
                updatePieChart();

                // 데이터 저장
                saveDataForToday(accumulatedCalories, mealAdapter.getMealItemList());


                // 현재 액티비티를 종료하고 결과를 전달
                Intent resultIntent = new Intent();
                resultIntent.putExtra("totalCalories", accumulatedCalories);
                setResult(RESULT_OK, resultIntent);
            }
        });

// 초기화 버튼 클릭 리스너 설정
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 누적 칼로리 초기화
                saveAccumulatedCalories(0);
                // 총 섭취 칼로리 업데이트
                updateTotalCalories();
                // RecyclerView 데이터 초기화
                mealAdapter.clearItems();
                // 파이 차트 초기화
                resetPieChart();
                Toast.makeText(MealInputActivity.this, "누적 칼로리가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });



        // "뒤로가기" 버튼 클릭 리스너 설정

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 뒤로 가기 기능 구현
                clearRecyclerView();
                finish();
            }
        });



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 검색 버튼 클릭 시 detail_info_textview 초기화
                detailInfoTextView.setText("");

                String DESC_KOR = searchEditText.getText().toString();
                if (!DESC_KOR.isEmpty()) {
                    new FetchNutritionTask().execute(DESC_KOR);
                } else {
                    Toast.makeText(MealInputActivity.this, "음식 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MealItem> selectedItems = new ArrayList<>();
                for (MealItem item : mealAdapter.getMealItemList()) {
                    if (item.isSelected()) {
                        selectedItems.add(item);
                    }
                }
                if (!selectedItems.isEmpty()) {
                    StringBuilder detailInfo = new StringBuilder();
                    for (MealItem item : selectedItems) {
                        detailInfo.append(String.format("음식 이름: %s\n칼로리: %s\n탄수화물: %s\n단백질: %s\n지방: %s\n당류: %s\n나트륨: %s\n\n",
                                item.getDescKor(), item.getNutrCont1(), item.getNutrCont2(), item.getNutrCont3(),
                                item.getNutrCont4(), item.getNutrCont5(), item.getNutrCont6()));
                    }
                    detailInfoTextView.setText(detailInfo.toString().trim());
                } else {
                    detailInfoTextView.setText("선택된 항목이 없습니다.");
                }
            }
        });

        // onResume에서 UI 초기화 및 데이터 로드
        onResume();

        // onCreate() 메서드에서 사용자의 성별에 따라 칼로리 제한 텍스트 설정
        String calorieLimitText = showRecommendedCalories();
        caloriestextview.setText(calorieLimitText);

        // showRecommendedCalories() 메서드를 호출하여 하루 섭취 권장 칼로리를 표시
        showRecommendedCalories();
    }


    // SharedPreferences에 데이터 저장하는 메서드
    private void saveDataToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 예시로 mealAdapter에서 mealItemList를 가져와서 저장
        Gson gson = new Gson();
        String mealItemListJson = gson.toJson(mealAdapter.getMealItemList());
        editor.putString("mealItemList", mealItemListJson);

        editor.apply();
    }

    protected void onPause() {
        super.onPause();
        // 액티비티가 화면에서 사라질 때 호출되는 메서드
        clearRecyclerView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        List<MealItem> savedMealItems = loadMealItems();
        mealAdapter.updateItems(savedMealItems);

        updatePieChart(); // 파이차트 업데이트

        updateTotalCalories(); // 총 섭취 칼로리 업데이트


        // 파이 차트 업데이트
        updatePieChart();

        // 사용자의 성별에 따라 하루 섭취 권장 칼로리를 표시
        String calorieLimitText = showRecommendedCalories();
        caloriestextview.setText(calorieLimitText);

    }


    private void clearRecyclerView() {
        mealAdapter = new MealAdapter(new ArrayList<>());
        recyclerView.setAdapter(mealAdapter); // 새로운 어댑터 설정
    }
    private double getAccumulatedCalories() {
        SharedPreferences sharedPreferences = getSharedPreferences("calorie_prefs", Context.MODE_PRIVATE);
        return Double.longBitsToDouble(sharedPreferences.getLong("accumulated_calories", Double.doubleToLongBits(0.0)));
    }

    private void saveAccumulatedCalories(double calories) {
        SharedPreferences sharedPreferences = getSharedPreferences("calorie_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("accumulated_calories", Double.doubleToLongBits(calories));
        editor.apply();
    }

    private void updatePieChart() {
        List<MealItem> selectedItems = mealAdapter.getMealItemList();
        ArrayList<PieEntry> entries = new ArrayList<>();

        // 선택된 항목들을 기반으로 파이차트 엔트리 추가
        for (MealItem item : selectedItems) {
            if (item.isSelected()) {
                float nutrCont2 = parseNutrientValue(item.getNutrCont2());
                float nutrCont3 = parseNutrientValue(item.getNutrCont3());
                float nutrCont4 = parseNutrientValue(item.getNutrCont4());

                addOrUpdateEntry(entries, "탄수화물 ", nutrCont2);
                addOrUpdateEntry(entries, "단백질 ", nutrCont3);
                addOrUpdateEntry(entries, "지방 ", nutrCont4);
            }
        }

        // PieData 설정 및 차트 업데이트
        PieDataSet dataSet = new PieDataSet(entries, "영양 성분");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(18f);

        PieData data = new PieData(dataSet);
        nutrientChart.setData(data);
        nutrientChart.invalidate(); // 차트 업데이트
    }


    private void addOrUpdateEntry(ArrayList<PieEntry> entries, String label, float value) {
        boolean found = false;
        for (PieEntry entry : entries) {
            if (entry.getLabel().equals(label)) {
                entry.setY(entry.getY() + value);
                found = true;
                break;
            }
        }
        if (!found) {
            entries.add(new PieEntry(value, label));
        }
    }


    private void resetPieChart() {
        // PieChart의 엔트리만 비우고 차트를 업데이트
        ArrayList<PieEntry> emptyEntries = new ArrayList<>();
        PieDataSet dataSet = new PieDataSet(emptyEntries, "영양 성분");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(18f);

        PieData data = new PieData(dataSet);
        nutrientChart.setData(data);
        nutrientChart.invalidate(); // 차트 업데이트
    }



    private float parseNutrientValue(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0f;
        }
    }

    private void updateTotalCalories() {
        double accumulatedCalories = getAccumulatedCalories();
        totalCaloriesTextView.setText("총 섭취 칼로리: " + accumulatedCalories + "kcal");
    }



    private void clearSavedData() {
        // Clear saved meal items and total calories from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("DailyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all saved data
        editor.apply();

        // Clear loaded items in mealAdapter
        mealAdapter.updateItems(new ArrayList<>());

        // Clear UI components if needed
        searchEditText.setText("");
        detailInfoTextView.setText("");
    }


    // 사용자의 성별을 가져오는 메서드
    private boolean isMale() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPageActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String gender = sharedPreferences.getString(MyPageActivity.KEY_GENDER, "");
        return gender.equals("male");
    }


    // 성별에 따라 하루 섭취 칼로리를 계산하고 보여주는 메서드
    private String showRecommendedCalories() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPageActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String gender = sharedPreferences.getString(MyPageActivity.KEY_GENDER, "");
        double recommendedCalories;
        String genderText = gender.equals("male") ? "남성" : "여성"; // 성별 텍스트 설정

        if (gender.equals("male")) {
            recommendedCalories = calculateRecommendedCaloriesForMale();
        } else if (gender.equals("female")) {
            recommendedCalories = calculateRecommendedCaloriesForFemale();
        } else {
            // 성별 정보가 없는 경우 기본값으로 설정
            recommendedCalories = calculateRecommendedCaloriesForDefault();
        }

        // 성별과 함께 하루 섭취 권장 칼로리를 반환
        return String.format(Locale.getDefault(), "하루 %s 섭취 권장 칼로리: %.2f kcal", genderText, recommendedCalories);
    }


    // 남성의 경우 하루 섭취 권장 칼로리를 계산하는 메서드
    private double calculateRecommendedCaloriesForMale() {
        // 남성의 경우 특정한 계산식을 사용하여 칼로리를 계산합니다. 예시로 2500 kcal을 반환합니다.
        return 2500;
    }

    // 여성의 경우 하루 섭취 권장 칼로리를 계산하는 메서드
    private double calculateRecommendedCaloriesForFemale() {
        // 여성의 경우 특정한 계산식을 사용하여 칼로리를 계산합니다. 예시로 2000 kcal을 반환합니다.
        return 2000;
    }

    // 성별 정보가 없는 경우 기본값으로 하루 섭취 권장 칼로리를 계산하는 메서드
    private double calculateRecommendedCaloriesForDefault() {
        // 성별 정보가 없는 경우 기본값으로 설정된 칼로리를 반환합니다. 예시로 2250 kcal을 반환합니다.
        return 2250;
    }

    private void saveDataForToday(double totalCalories, List<MealItem> mealItems) {
        SharedPreferences sharedPreferences = getSharedPreferences("DailyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("totalCalories", (float) totalCalories);

        // 영양 정보와 함께 선택된 여부를 JSON 문자열로 변환하여 저장
        Gson gson = new Gson();
        String mealItemsJson = gson.toJson(mealItems);
        editor.putString("mealItems", mealItemsJson);

        // 현재 시간을 밀리초로 저장
        long currentTimeMillis = System.currentTimeMillis();
        editor.putLong("savedTime", currentTimeMillis);

        editor.apply();
    }


    private double loadTotalCalories() {
        SharedPreferences sharedPreferences = getSharedPreferences("DailyData", Context.MODE_PRIVATE);
        return sharedPreferences.getFloat("totalCalories", 0);
    }

    private List<MealItem> loadMealItems() {
        SharedPreferences sharedPreferences = getSharedPreferences("DailyData", Context.MODE_PRIVATE);
        String mealItemsJson = sharedPreferences.getString("mealItems", "");

        if (!mealItemsJson.isEmpty()) {
            Gson gson = new Gson();
            Type mealItemListType = new TypeToken<ArrayList<MealItem>>() {}.getType();
            List<MealItem> loadedItems = gson.fromJson(mealItemsJson, mealItemListType);

            // 선택된 항목을 로드한 후에도 선택된 상태를 유지하도록 설정
            for (MealItem item : loadedItems) {
                if (item.isSelected()) {
                    item.setSelected(true);
                }
            }

            return loadedItems;
        } else {
            return new ArrayList<>();
        }
    }





    // 선택된 음식들의 총 섭취 칼로리를 계산하는 메서드
    private double calculateTotalCalories() {
        double totalCalories = 0;
        for (MealItem item : mealAdapter.getMealItemList()) {
            if (item.isSelected()) {
                // 선택된 음식의 칼로리 값을 가져와 총 칼로리에 더함
                totalCalories += Double.parseDouble(item.getNutrCont1());
            }
        }
        return totalCalories;
    }


    private class FetchNutritionTask extends AsyncTask<String, Void, List<MealItem>> {
        @Override
        protected List<MealItem> doInBackground(String... params) {
            String DESC_KOR = params[0];
            String apiUrl = "https://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1/getFoodNtrItdntList1?serviceKey=1rbxWx4B0FrrB5%2FT4VxjOVwn6sczE%2FKvOHp5eTu%2BFdvdHg5Ipzt8fjyJJyRNf8m6NmVVjz1XPt%2FnVypsRj3FSA%3D%3D&desc_kor=" + DESC_KOR + "&pageNo=1&numOfRows=100";

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return parseXML(response.toString());
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }



        @Override
        protected void onPostExecute(List<MealItem> result) {
            if (result != null) {
                if (result.isEmpty()) {
                    Toast.makeText(MealInputActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    mealAdapter.updateItems(result);
                    updatePieChart(); // 파이 차트 업데이트
                }
            } else {
                Toast.makeText(MealInputActivity.this, "음식 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        }


        private List<MealItem> parseXML(String xml) {
            List<MealItem> mealItems = new ArrayList<>();
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new java.io.StringReader(xml));

                int eventType = parser.getEventType();
                MealItem currentMeal = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagName;
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            tagName = parser.getName();
                            if (tagName.equals("item")) {
                                currentMeal = new MealItem();
                            } else if (currentMeal != null) {
                                switch (tagName) {
                                    case "DESC_KOR":
                                        currentMeal.setDescKor(parser.nextText());
                                        break;
                                    case "SERVING_WT":
                                        currentMeal.setServingWt(parser.nextText());
                                        break;
                                    case "NUTR_CONT1":
                                        currentMeal.setNutrCont1(parser.nextText());
                                        break;
                                    case "NUTR_CONT2":
                                        currentMeal.setNutrCont2(parser.nextText());
                                        break;
                                    case "NUTR_CONT3":
                                        currentMeal.setNutrCont3(parser.nextText());
                                        break;
                                    case "NUTR_CONT4":
                                        currentMeal.setNutrCont4(parser.nextText());
                                        break;
                                    case "NUTR_CONT5":
                                        currentMeal.setNutrCont5(parser.nextText());
                                        break;
                                    case "NUTR_CONT6":
                                        currentMeal.setNutrCont6(parser.nextText());
                                        break;
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            tagName = parser.getName();
                            if (tagName.equals("item") && currentMeal != null) {
                                mealItems.add(currentMeal);
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mealItems;
        }
    }
}