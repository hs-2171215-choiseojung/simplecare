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

    private EditText searchEditText;
    private WebView youtubeWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_record);

        searchEditText = findViewById(R.id.search_edittext);
        youtubeWebView = findViewById(R.id.youtube_webview);

        youtubeWebView.setWebViewClient(new MyWebViewClient());
        youtubeWebView.getSettings().setJavaScriptEnabled(true);
        youtubeWebView.getSettings().setDomStorageEnabled(true);
        youtubeWebView.getSettings().setLoadsImagesAutomatically(true);

        findViewById(R.id.search_button).setOnClickListener(v -> performSearch());
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        if (!query.isEmpty()) {
            try {
                String encodedQuery = URLEncoder.encode(query, "UTF-8");
                youtubeWebView.loadUrl("https://www.youtube.com/results?search_query=" + encodedQuery);
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(this, "검색어 인코딩에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.startsWith("https://www.youtube.com/")) {
                return false;
            } else if (url.startsWith("https://example.com/")) {
                Toast.makeText(ExerciseRecordActivity.this, "내부 처리: " + url, Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
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
