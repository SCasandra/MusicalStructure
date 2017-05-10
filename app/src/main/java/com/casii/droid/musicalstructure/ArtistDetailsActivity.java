package com.casii.droid.musicalstructure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class ArtistDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);
        getSupportActionBar().setTitle(getIntent().getStringExtra("NAME"));
        String url = getIntent().getStringExtra("URL");
        WebView webview = (WebView) findViewById(R.id.web_view);
        webview.loadUrl(url);
    }
}
