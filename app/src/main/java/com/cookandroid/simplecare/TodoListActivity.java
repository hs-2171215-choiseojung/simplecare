package com.cookandroid.simplecare;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class TodoListActivity extends AppCompatActivity {

    private TextView tvSelectedDate;
    private ListView todoListView;
    private ArrayAdapter<String> todoListAdapter;
    private TodoDataSource dataSource;
    private String selectedDate;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        todoListView = findViewById(R.id.todoListView);

        dataSource = new TodoDataSource(this);
        dataSource.open();
        backButton = findViewById(R.id.backButton);

        todoListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        todoListView.setAdapter(todoListAdapter);

        selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        tvSelectedDate.setText(selectedDate);

        loadTodoList(selectedDate);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // 현재 액티비티를 종료하고 이전 화면으로 돌아갑니다.
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

    private void loadTodoList(String selectedDate) {
        List<String> todos = dataSource.getAllTodos(selectedDate);
        todoListAdapter.clear();
        todoListAdapter.addAll(todos);
        todoListAdapter.notifyDataSetChanged();
    }
}