package com.appart.hp.nearme;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

        Button login,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //hiding titlebar
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(com.appart.hp.menuintegration.R.layout.activity_first);

        login = (Button) findViewById(com.appart.hp.menuintegration.R.id.button_login);
        signup = (Button) findViewById(com.appart.hp.menuintegration.R.id.button_signup);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });
    }
}
