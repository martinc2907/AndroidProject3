package com.example.user.androidlock;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LockScreenActivity extends AppCompatActivity {
    FloatingActionButton unlockBtn;
    TextView ticktock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lockscreen);

        ticktock = findViewById(R.id.ticktock);
        unlockBtn = findViewById(R.id.unlock_btn);

        unlockBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

                Toast.makeText(getApplicationContext(),"You gave up!", Toast.LENGTH_LONG).show();
                //Show some destruction animation.
            }
        });

        int seconds = getIntent().getIntExtra("TIME",10);
        CountDownTimer countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millis) {
                ticktock.setText( Integer.toString((int) (millis / 1000)));
            }

            @Override
            public void onFinish() {
                ticktock.setText("Finished");
            }
        }.start();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

}
