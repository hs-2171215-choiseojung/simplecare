package com.cookandroid.simplecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

public class ExerciseDietTrackerActivity extends AppCompatActivity {

    private Button saveButton;
    private Button backButton;
    private Button buttonAdd;
    private CalendarView calendarView;
    private ListView todoListView;
    private EditText editTextTitle;
    private TextView selectedDateTextView;

    private ArrayAdapter<String> todoListAdapter;
    private TodoDataSource dataSource;
    private String currentSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_diet_tracker);

        // 데이터 소스 초기화 및 열기
        dataSource = new TodoDataSource(this);
        dataSource.open();

        // 뷰 초기화
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);
        calendarView = findViewById(R.id.calendarView);
        todoListView = findViewById(R.id.todoListView);
        editTextTitle = findViewById(R.id.editTextTitle);
        buttonAdd = findViewById(R.id.buttonAdd);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);

        // 어댑터 초기화
        todoListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        todoListView.setAdapter(todoListAdapter);

        // 날짜 선택 리스너 설정
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                currentSelectedDate = year + "년 " + (month + 1) + "월 " + dayOfMonth + "일";
                selectedDateTextView.setText("선택된 날짜: " + currentSelectedDate);

                // TodoListActivity로 이동
                Intent intent = new Intent(ExerciseDietTrackerActivity.this, TodoListActivity.class);
                intent.putExtra("SELECTED_DATE", currentSelectedDate); // 선택된 날짜 데이터 전달
                startActivity(intent); // 다음 화면으로 이동
            }
        });

        // 추가 버튼 클릭 리스너 설정
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoTitle = editTextTitle.getText().toString().trim();
                if (!todoTitle.isEmpty() && currentSelectedDate != null) {
                    long insertedId = dataSource.addTodo(todoTitle, currentSelectedDate);
                    if (insertedId != -1) {
                        loadTodoList(currentSelectedDate);
                        editTextTitle.getText().clear();
                        Toast.makeText(ExerciseDietTrackerActivity.this, "할 일이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ExerciseDietTrackerActivity.this, "할 일을 추가하는데 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ExerciseDietTrackerActivity.this, "할 일을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 리스트 항목 클릭 리스너 설정
        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String todo = (String) parent.getItemAtPosition(position); // 클릭된 항목 가져오기
                dataSource.deleteTodo(todo, currentSelectedDate); // 데이터베이스에서 해당 항목 삭제
                loadTodoList(currentSelectedDate); // 변경된 식단 목록 다시 로드
                Toast.makeText(ExerciseDietTrackerActivity.this, "할 일이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 뒤로 가기 버튼 클릭 리스너 설정
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 초기화: 오늘 날짜로 설정
        long selectedDateMillis = calendarView.getDate();
        currentSelectedDate = convertMillisToDate(selectedDateMillis);
        selectedDateTextView.setText("선택된 날짜: " + currentSelectedDate);
        loadTodoList(currentSelectedDate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

    // 할 일 목록 로드
    private void loadTodoList(String selectedDate) {
        List<String> todos = dataSource.getAllTodos(selectedDate); // 선택된 날짜의 할 일 목록 가져오기
        todoListAdapter.clear(); // 어댑터 초기화
        todoListAdapter.addAll(todos); // 새로운 데이터로 어댑터 설정
        todoListAdapter.notifyDataSetChanged(); // 데이터 변경을 어댑터에 알림
    }

    // 밀리초를 날짜 형식으로 변환
    private String convertMillisToDate(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "년 " + month + "월 " + dayOfMonth + "일";
    }
}
