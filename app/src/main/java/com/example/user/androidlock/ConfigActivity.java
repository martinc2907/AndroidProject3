package com.example.user.androidlock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.user.androidlock.R;

/**
 * Created by user on 2018-01-06.
 */

public class ConfigActivity extends Activity {

    private Button lock_btn;
    private EditText timer;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asdf);

        lock_btn = findViewById(R.id.lock_btn);
        timer = findViewById(R.id.timer_input);

        lock_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String text = timer.getText().toString();
                int seconds = Integer.valueOf(text);
                Intent intent = new Intent(ConfigActivity.this, LockScreenActivity.class);
                intent.putExtra("TIME", seconds);
                startActivity(intent);

            }
        });
    }
}
