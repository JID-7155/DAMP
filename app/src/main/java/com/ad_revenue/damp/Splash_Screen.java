package com.ad_revenue.damp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ad_revenue.damp.Services.JSONService;


public class Splash_Screen extends AppCompatActivity {

    ArrayAdapter<String> namesAdapter;
    String nameToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        Context myContext = getApplicationContext();
        String[] patientNames = new JSONService(){}.getPatientNames(myContext);

        Spinner spinner = (Spinner) findViewById(R.id.patientOptions);
        namesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, patientNames);
        namesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(namesAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                nameToPass = adapter.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                nameToPass = "New Patient";
            }
        });

    }

    public void editPlans(View view) {
        Intent intent = new Intent(this, View_Plans.class);
        intent.putExtra("patientName", nameToPass);
        startActivity(intent);
    }
}
