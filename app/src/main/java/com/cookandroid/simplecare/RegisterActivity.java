package com.cookandroid.simplecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText join_email;
    private Button check_button, join_button, delete_button; // delete 버튼 추가
    private DatabaseHelper databaseHelper;
    private boolean validate = false; // validate 변수 선언 및 초기화

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        databaseHelper = new DatabaseHelper(this);

        join_email = findViewById(R.id.join_email);
        check_button = findViewById(R.id.check_button);
        join_button = findViewById(R.id.join_button);
        delete_button = findViewById(R.id.delete); // delete 버튼 참조

        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = join_email.getText().toString();
                if (userEmail.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (databaseHelper.checkEmailExists(userEmail)) {
                    Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                    validate = false; // 중복 확인이 완료되지 않았음을 표시
                } else {
                    Toast.makeText(RegisterActivity.this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                    validate = true; // 중복 확인이 완료되었음을 표시
                }
            }
        });

        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate) {
                    Toast.makeText(RegisterActivity.this, "아이디 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = ((EditText) findViewById(R.id.join_password)).getText().toString();
                String passwordCheck = ((EditText) findViewById(R.id.join_pwck)).getText().toString();
                if (!password.equals(passwordCheck)) {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 사용자가 입력한 이메일과 비밀번호를 데이터베이스에 저장
                String email = join_email.getText().toString();
                databaseHelper.addUser(email, password);

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // RegisterActivity 종료
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // RegisterActivity 종료
            }
        });
    }
}