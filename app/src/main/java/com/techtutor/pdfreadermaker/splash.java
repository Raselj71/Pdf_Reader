package com.techtutor.pdfreadermaker;



import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;


public class splash extends AppCompatActivity {
    private static final long COUNTER_TIME = 3;
    private long secondsRemaining;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        createTimer(COUNTER_TIME);

    }


    private void createTimer(long seconds) {


        CountDownTimer countDownTimer =
                new CountDownTimer(seconds * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        secondsRemaining = ((millisUntilFinished / 1000) + 1);

                    }

                    @Override
                    public void onFinish() {
                        secondsRemaining = 0;


                        Application application = getApplication();

                        // If the application is not an instance of MyApplication, log an error message and
                        // start the MainActivity without showing the app open ad.

                        // Show the app open ad.

                        startMainActivity();




                    }


                };
        countDownTimer.start();
    }
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        finish();
    }

}