package com.appart.hp.nearme;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by HP on 7/14/2017.
 */

public class HomeActivity extends MenuActivity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(com.appart.hp.menuintegration.R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(com.appart.hp.menuintegration.R.layout.activity_home, contentFrameLayout);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        tv = (TextView) findViewById(R.id.text_home);
//        tv.setText("Text view of home activity");
    }
}
