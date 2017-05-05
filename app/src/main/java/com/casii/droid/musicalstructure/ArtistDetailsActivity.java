package com.casii.droid.musicalstructure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class ArtistDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getIntent().getStringExtra("NAME"));
        String url = getIntent().getStringExtra("URL");
        WebView webview = new WebView(this);
        setContentView(webview);
        webview.loadUrl(url);
    }
}
