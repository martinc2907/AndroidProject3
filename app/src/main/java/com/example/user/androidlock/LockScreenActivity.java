package com.example.user.androidlock;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LockScreenActivity extends AppCompatActivity{
    FloatingActionButton unlockBtn;
    TextView ticktock;
    TextView hr;
    TextView min;
    TextView hrText;
    TextView minText;
    private HomeKeyLocker mHomeKeyLocker;
    private AlertDialog.Builder builder;
    private AlertDialog popup;
    private CountDownTimer countDownTimer;
    boolean success = true;
    RelativeLayout ll;
    int img_id;
    TextView done;
    int reward;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.lockscreen);

        img_id = getIntent().getIntExtra("IMG",0);
        if(img_id!=0) {
            ll = findViewById(R.id.back);
            ll.setBackgroundResource(getIntent().getIntExtra("IMG", 0));
        }

        mHomeKeyLocker = new HomeKeyLocker();
        mHomeKeyLocker.lock(this);

        if(!Settings.canDrawOverlays(getApplicationContext())){
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
        }

        //ticktock = findViewById(R.id.ticktock);
        unlockBtn = findViewById(R.id.unlock_btn);

        unlockBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

                builder = new AlertDialog.Builder(LockScreenActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.custom_alert,null);
                Button yesButton = mView.findViewById(R.id.yes_btn);
                Button noButton = mView.findViewById(R.id.no_btn);
                builder.setView(mView);
                popup = builder.create();
                popup.show();

                yesButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        success = false;
                        countDownTimer.onFinish();
                    }
                });

                noButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        popup.cancel();
                    }
                });
            }
        });

        int hours = getIntent().getIntExtra("hours",0);
        int mins = getIntent().getIntExtra("minutes",0);
        hr = findViewById(R.id.tick);
        min = findViewById(R.id.tock);
        hrText = findViewById(R.id.tickText);
        minText = findViewById(R.id.tockText);
        done = findViewById(R.id.done);
        reward = (hours*3600+mins*60)/100;

        countDownTimer = new CountDownTimer((hours*60*60 + mins*60)*1000+1000, 1000){
            @Override
            public void onTick(long millis){
                long secs = millis/1000;
                long h = secs/3600;
                long m = (secs - h*3600)/60;
                hr.setText(Long.toString(h));
                min.setText(Long.toString(m));
            }

            @Override
            public void onFinish(){
                this.cancel();
                if(popup!=null)
                    popup.cancel();
                unlockBtn.setVisibility(View.GONE);
                min.setVisibility(View.GONE);
                hr.setVisibility(View.GONE);
                hrText.setVisibility(View.GONE);
                minText.setVisibility(View.GONE);

                //Show success/failure image
                ll = findViewById(R.id.back);
                if(success){
                    //ll.setBackgroundResource(R.drawable.test1);
                    done.setText("+" + Integer.toString(reward));
                    done.setVisibility(View.VISIBLE);
                }else{
                    //ll.setBackgroundResource(R.drawable.test2);
                    done.setText("Try again next time!");
                    done.setVisibility(View.VISIBLE);
                }

                //Go back to previous screen
                mHomeKeyLocker.unlock();
                Intent i= new Intent(LockScreenActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                finish();
                success = true;
            }
        }.start();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    @Override
    protected void onPause(){
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    /*
    @Override
    public void onBackPressed(){
        return;
    }
*/
    /*
    @Override
    public void onAttachedToWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

*/


}
