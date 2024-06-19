package com.cookandroid.simplecare;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ExerciseRecordActivity extends AppCompatActivity {

    private EditText searchEditText; // 검색어 입력창
    private Button searchButton; // 검색 버튼
    private Button backButton; // 뒤로 가기 버튼
    private WebView youtubeWebView; // 유튜브 웹뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_record);

        // 뷰 초기화
        searchEditText = findViewById(R.id.search_edittext);
        searchButton = findViewById(R.id.search_button);
        backButton = findViewById(R.id.back_button);
        youtubeWebView = findViewById(R.id.youtube_webview);

        // 웹뷰 설정
        youtubeWebView.setWebViewClient(new MyWebViewClient()); // 커스텀 WebViewClient 설정
        youtubeWebView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 활성화
        youtubeWebView.getSettings().setDomStorageEnabled(true); // DOM 스토리지 활성화
        youtubeWebView.getSettings().setLoadsImagesAutomatically(true); // 이미지 자동 로드 활성화

        // 검색 버튼 클릭 리스너 설정
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    try {
                        // 검색어 인코딩
                        String encodedQuery = URLEncoder.encode(query, "UTF-8");
                        // 유튜브 검색 결과 로드
                        youtubeWebView.loadUrl("https://www.youtube.com/results?search_query=" + encodedQuery);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(ExerciseRecordActivity.this, "검색어 인코딩에 실패했습니다", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ExerciseRecordActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 뒤로 가기 버튼 클릭 리스너 설정
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // WebViewClient를 상속받은 커스텀 클래스 정의
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.startsWith("https://www.youtube.com/")) {
                return false;
            } else if (url.startsWith("https://example.com/")) {

                Toast.makeText(ExerciseRecordActivity.this, "내부 처리: " + url, Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }
    }
}
