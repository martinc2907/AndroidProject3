package com.example.user.androidlock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

/**
 * Created by user on 2018-01-06.
 */

public class MainActivity extends Activity {

    private Button lock_btn;
    private EditText timer;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    private int main_img;
    private Button test_btn;
    private boolean backFromGallery;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /* Shared Preferences */
        mSettings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        //mSettings.edit().clear().commit();

        if(!mSettings.getBoolean("firstTime",false)){
            Log.e("TAGGER", "Initialised");
            //Run code once
            init_data();
        }
        main_img = mSettings.getInt("main_img",0);

        lock_btn = findViewById(R.id.lock_btn);
        timer = findViewById(R.id.timer_input);

        /* Test button that changes screen lock background upon click. */
        test_btn = findViewById(R.id.test_btn);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_img++;
                mSettings.edit().putInt("main_img", main_img).commit();
            }
        });

        lock_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String text = timer.getText().toString();
                timer.setText("");
                int seconds = Integer.valueOf(text);
                Intent intent = new Intent(MainActivity.this, LockScreenActivity.class);
                intent.putExtra("TIME", seconds);
                intent.putExtra("IMG", main_img);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!backFromGallery)
            overridePendingTransition(R.anim.fade_in_long,R.anim.fade_out_long);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onResume();

        //If coming back from gallery activity.
        if(requestCode != 1) {
            mSettings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
            mSettings.getInt("main_img", 0);
            backFromGallery = true; //set flag.
        }
        //If coming back from screen lock.
        else{
            backFromGallery = false;    //set flag for animation.
        }
    }

    private void init_data(){
        //Flag for checked whether initialised.
        editor = mSettings.edit();
        editor.putBoolean("firstTime",true);

        Background b1 = new Background(R.drawable.img1, 1000, false, "cute");
        Background b2 = new Background(R.drawable.img2, 1000, false, "cute");
        Background b3 = new Background(R.drawable.img3, 1000, false, "cute");
        Background b4 = new Background(R.drawable.img4, 1000, false, "cute");
        Background b5 = new Background(R.drawable.img5, 1000, false, "cute");

        Gson gson = new Gson();
        String json;

        main_img = R.drawable.lock_default;
        editor.putInt("main_img", R.drawable.img1);

        json = gson.toJson(b1);
        editor.putString("b1",json);
        json = gson.toJson(b2);
        editor.putString("b2",json);
        json = gson.toJson(b3);
        editor.putString("b3",json);
        json = gson.toJson(b4);
        editor.putString("b4",json);
        json = gson.toJson(b5);
        editor.putString("b5",json);
        editor.commit();    //Call once? or every time?
    }
}
