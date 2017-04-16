package com.ad_revenue.damp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        //yeah boiiii
    }

    public void editPlans(View view) {
        Intent intent = new Intent(this, View_Plans.class);
        startActivity(intent);
    }
}
