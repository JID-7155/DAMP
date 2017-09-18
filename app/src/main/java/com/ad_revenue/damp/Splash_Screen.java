package com.ad_revenue.damp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(" ");
    }

    public void editPlans(View view) {
        Intent intent = new Intent(this, View_Plans.class);
        startActivity(intent);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(48,124,239)));
        }
    }

    public void emergencyPlans(View view) { //For "Emergency Mode" button press.
        if(getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(255,0,0)));
        }


        Intent intent = new Intent(this, View_Plans.class);
        startActivity(intent);
    }
}
