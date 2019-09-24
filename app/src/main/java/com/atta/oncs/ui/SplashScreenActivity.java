package com.atta.oncs.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.atta.oncs.R;
import com.atta.oncs.model.SessionManager;

import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {


    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    // Session Manager Class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        changeLanguage();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity



                SessionManager.getInstance(SplashScreenActivity.this).checkLogin();

                // close this activity
                finish();

            }
        }, SPLASH_TIME_OUT);
    }


    private void changeLanguage() {
        Locale locale = new Locale("ar");
        SessionManager.getInstance(this).setLanguage("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());
    }
}
