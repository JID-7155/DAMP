package com.ad_revenue.damp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
    }

    public void editPlans(View view) {
        Intent intent = new Intent(this, View_Plans.class);
        startActivity(intent);
    }
}
