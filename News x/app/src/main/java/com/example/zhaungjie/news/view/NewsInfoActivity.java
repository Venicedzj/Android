package com.example.zhaungjie.news.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.example.zhaungjie.news.R;

/**
 * Created by zhaungjie on 17-4-10.
 */

public class NewsInfoActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_info);
        initViews();

        ImageButton imageButton=(ImageButton)findViewById(R.id.backbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void initViews(){
        webView = (WebView) this.findViewById(R.id.infocontent);

        Intent intent = this.getIntent();

        String newsUrl = intent.getStringExtra("newsUrl");

        webView.loadUrl(newsUrl);

    }
}