package com.cookandroid.simplecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText login_email, login_password;
    private Button login_button, join_button;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseHelper = new DatabaseHelper(this);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);

        join_button = findViewById(R.id.join_button);
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = login_email.getText().toString();
                String userPwd = login_password.getText().toString();

                if (databaseHelper.checkCredentials(userEmail, userPwd)) {
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, p1.class); // p1 액티비티로 이동
                    startActivity(intent); // p1 액티비티로 이동
                    finish(); // LoginActivity 종료
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
