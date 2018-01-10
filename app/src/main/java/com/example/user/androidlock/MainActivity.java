package com.example.user.androidlock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

/**
 * Created by user on 2018-01-06.
 */

public class MainActivity extends Activity {

    private FloatingActionButton lock_btn;
    private EditText timer;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    private int main_img;
    private Button test_btn;
    private boolean backFromGallery;
    private int money;
    private NumberPicker hr;
    private NumberPicker min;
    private FloatingActionButton preview;
    private RelativeLayout rl;

    @SuppressLint("ClickableViewAccessibility")
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
            init_data();
        }
        //init_data();

        main_img = mSettings.getInt("main_img",0);
        lock_btn = findViewById(R.id.lock_btn);
        preview = findViewById(R.id.preview);
        rl = findViewById(R.id.rl);


        hr = findViewById(R.id.hr);
        hr.setMinValue(0);
        hr.setMaxValue(12);

        min = findViewById(R.id.min);
        min.setMinValue(0);
        min.setMaxValue(59);

        /* Test button that changes screen lock background upon click. */
        test_btn = findViewById(R.id.test_btn);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_img++;
                mSettings.edit().putInt("main_img", main_img).commit();
            }
        });


        preview.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                String temp = rl.getBackground().toString();
                String temp2 = temp.replace("android.graphics.drawable.BitmapDrawable@","");
                int savePrev = Integer.parseInt(temp2,16);
                //int savePrev = Integer.decode(temp2);
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //PRESSED
                        rl.setBackgroundResource(main_img);
                        hideViews();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //RELEASED
                        rl.setBackgroundResource(R.drawable.gradient2);
                        showViews();
                        return true;
                    default:
                        return false;
                }
            }
        });

        lock_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int hours = hr.getValue();
                int mins = min.getValue();

                hr.setValue(0);
                min.setValue(0);

                Intent intent = new Intent(MainActivity.this, LockScreenActivity.class);
                intent.putExtra("hours",hours);
                intent.putExtra("minutes",mins);
                intent.putExtra("IMG",main_img);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
            main_img = mSettings.getInt("main_img", 0);
            backFromGallery = true; //set flag.
            money = mSettings.getInt("money",0);
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
        Background b4 = new Background(R.drawable.img3, 1000, false, "cute");
        Background b5 = new Background(R.drawable.img4, 1000, false, "cute");

        Gson gson = new Gson();
        String json;

        main_img = R.drawable.lock_default;
        editor.putInt("main_img", R.drawable.img1);

        money = 99999;
        editor.putInt("money", money);

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

    public void hideViews(){
        FloatingActionMenu menu = findViewById(R.id.menu);
        TextView tv1 = findViewById(R.id.hrsText);
        TextView tv2 = findViewById(R.id.minsText);
        TextView tv3 = findViewById(R.id.title);
        Button b1 = findViewById(R.id.money);

        lock_btn.setVisibility(View.INVISIBLE);
        hr.setVisibility(View.INVISIBLE);
        min.setVisibility(View.INVISIBLE);
        menu.setVisibility(View.INVISIBLE);
        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        //tv3.setVisibility(View.INVISIBLE);
        tv3.setText("PREVIEW");
        b1.setVisibility(View.INVISIBLE);



    }

    public void showViews(){
        FloatingActionMenu menu = findViewById(R.id.menu);
        TextView tv1 = findViewById(R.id.hrsText);
        TextView tv2 = findViewById(R.id.minsText);
        TextView tv3 = findViewById(R.id.title);
        Button b1 = findViewById(R.id.money);

        lock_btn.setVisibility(View.VISIBLE);
        hr.setVisibility(View.VISIBLE);
        min.setVisibility(View.VISIBLE);
        menu.setVisibility(View.VISIBLE);
        tv1.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.VISIBLE);
        //tv3.setVisibility(View.VISIBLE);
        tv3.setText("Study Lock");
        b1.setVisibility(View.VISIBLE);

    }
}
