package com.atta.oncs.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.atta.oncs.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.myNavHostFragment);
        fragment.onActivityResult(requestCode, resultCode, data);

    }*/
}
