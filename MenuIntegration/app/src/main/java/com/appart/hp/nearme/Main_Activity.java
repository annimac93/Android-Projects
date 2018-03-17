package com.appart.hp.nearme;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by HP on 7/14/2017.
 */

public class Main_Activity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //hiding titlebar
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(com.appart.hp.menuintegration.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(com.appart.hp.menuintegration.R.id.toolbar);
        setSupportActionBar(toolbar);
        new CountDownTimer(2000,1000) {

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                Intent myIntent=new Intent(Main_Activity.this,FirstActivity.class);
                startActivity(myIntent);
                finish();
            }

            @Override
            public void onTick(long arg0) {
                // TODO Auto-generated method stub

            }
        }.start();
    }
}
