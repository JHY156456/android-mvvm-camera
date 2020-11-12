package com.example.mvvmappapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mvvmappapplication.ui.user.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView = findViewById(R.id.iv_splash);

        Glide.with(this)
                .asGif()
                .load(R.raw.ebichu)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
        startLoading();
    }
    private void goLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                goLoginActivity();
            }
        }, 2000);
    }
}
