package com.cookandroid.simplecare;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class IntermittentFastingActivity extends AppCompatActivity {

    private EditText hoursEditText;
    private EditText minutesEditText;
    private Button startButton;
    private Button endButton;
    private Button pauseButton;
    private TextView timerTextView;
    private ProgressBar progressBar;
    private TextView completedCountTextView;
    private TextView goalTimeTextView;

    private CountDownTimer fastingTimer;
    private long fastingTimeInMillis;
    private boolean isFasting = false;
    private boolean isPaused = false;
    private long timeRemainingInMillis;
    private int completedCount = 0;
    private int goalTimeHours = 0;

    private SharedPreferences preferences;
    private static final String PREF_FASTING_TIME = "fasting_time";
    private static final String PREF_COMPLETED_COUNT = "completed_count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermittent_fasting);

        // UI 요소 초기화
        hoursEditText = findViewById(R.id.hours_edit_text);
        minutesEditText = findViewById(R.id.minutes_edit_text);
        startButton = findViewById(R.id.start_button);
        endButton = findViewById(R.id.end_button);
        pauseButton = findViewById(R.id.pause_button);
        timerTextView = findViewById(R.id.timer_text_view);
        progressBar = findViewById(R.id.progress_bar);
        completedCountTextView = findViewById(R.id.completed_count_text_view);
        goalTimeTextView = findViewById(R.id.goal_time_text_view); // 목표 시간 TextView 연결

        // SharedPreferences 초기화
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // 초기 목표 시간 가져오기
        goalTimeHours = preferences.getInt(PREF_FASTING_TIME, 0);
        updateGoalTime();

        // 완료 횟수 초기화 및 업데이트
        completedCount = preferences.getInt(PREF_COMPLETED_COUNT, 0);
        updateCompletedCount();

        // "시작하기" 버튼 클릭 리스너
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFasting();
            }
        });

        // "리셋" 버튼 클릭 리스너
        findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCompletedCount(v);
            }
        });

        // "종료하기" 버튼 클릭 리스너
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endFasting();
            }
        });

        // "일시 정지" 버튼 클릭 리스너
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseFasting();
            }
        });

        // "뒤로 가기" 버튼 클릭 리스너
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 목표 시간 업데이트
    private void updateGoalTime() {
        goalTimeTextView.setText("목표 시간: " + goalTimeHours + "시간");
    }

    // 단식 시작
    private void startFasting() {
        String hoursString = hoursEditText.getText().toString().trim();
        String minutesString = minutesEditText.getText().toString().trim();

        int hours = 0;
        int minutes = 0;

        if (!hoursString.isEmpty()) {
            hours = Integer.parseInt(hoursString);
        }

        if (!minutesString.isEmpty()) {
            minutes = Integer.parseInt(minutesString);
        }

        // 입력된 시간을 밀리초로 변환하여 계산
        fastingTimeInMillis = (hours * 60 + minutes) * 60 * 1000;
        timeRemainingInMillis = fastingTimeInMillis;

        // goalTimeHours 업데이트
        goalTimeHours = hours;

        // goalTimeTextView 업데이트
        updateGoalTime();

        // 단식 경고 표시
        showWarningDialog();

        if (hours > 0 || minutes > 0) {
            isFasting = true;
            startButton.setVisibility(View.GONE);
            endButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.VISIBLE);

            startTimer();
        } else {
            Toast.makeText(this, "유효한 단위 시간을 입력하세요", Toast.LENGTH_SHORT).show();
        }
    }

    // 완료 횟수 리셋
    public void resetCompletedCount(View view) {
        completedCount = 0;
        preferences.edit().putInt(PREF_COMPLETED_COUNT, completedCount).apply();
        updateCompletedCount();
        Toast.makeText(this, "완료 횟수가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
    }

    // 경고 다이얼로그 표시
    private void showWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("단식 경고")
                .setMessage("단식을 진행하기 전에 건강에 유의하고 올바른 방법으로 진행하세요.")
                .setPositiveButton("확인", null)
                .show();
    }

    // 타이머 시작
    private void startTimer() {
        fastingTimer = new CountDownTimer(timeRemainingInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isPaused) {
                    timeRemainingInMillis = millisUntilFinished;
                    updateTimerText(millisUntilFinished);
                    updateProgressBar(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                // 완료 횟수 증가 및 저장
                completedCount++;
                preferences.edit().putInt(PREF_COMPLETED_COUNT, completedCount).apply();
                updateCompletedCount();
                endFasting();
            }
        }.start();
    }

    // 프로그레스바 업데이트
    private void updateProgressBar(long millisUntilFinished) {
        long totalTimeInMillis = fastingTimeInMillis;
        int progress = (int) (((float) (totalTimeInMillis - millisUntilFinished) / totalTimeInMillis) * 100);
        progressBar.setProgress(progress);
    }

    // 단식 일시 정지 및 재개
    private void pauseFasting() {
        if (isPaused) {
            isPaused = false;
            pauseButton.setText("일시 정지");
            startTimer();
        } else {
            isPaused = true;
            pauseButton.setText("재개");
            fastingTimer.cancel();
        }
    }

    // 단식 종료
    private void endFasting() {
        isFasting = false;
        isPaused = false;
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        pauseButton.setText("일시 정지");
        endButton.setVisibility(View.GONE);
        timerTextView.setText("남은 시간: 00:00:00");
        hoursEditText.setText("");
        minutesEditText.setText("");
        progressBar.setProgress(0);

        // 목표 시간 초기화
        goalTimeHours = 0;
        updateGoalTime();

        if (fastingTimer != null) {
            fastingTimer.cancel();
        }
    }

    // 완료 횟수 업데이트
    private void updateCompletedCount() {
        completedCountTextView.setText("완료 횟수: " + completedCount);
    }

    // 타이머 텍스트 업데이트
    private void updateTimerText(long millisUntilFinished) {
        long hours = millisUntilFinished / (1000 * 3600);
        long minutes = (millisUntilFinished % (1000 * 3600)) / (1000 * 60);
        long seconds = (millisUntilFinished % (1000 * 60)) / 1000;

        String remainingTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView.setText("남은 시간: " + remainingTime);
    }
}
