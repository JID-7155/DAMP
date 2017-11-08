package com.ad_revenue.damp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ad_revenue.damp.Services.JSONService;

public class Splash_Screen extends AppCompatActivity {

    ArrayAdapter<String> namesAdapter;
    String nameToPass;
    TextView userInputText;
    Context myContext;
    String[] patientNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        myContext = getApplicationContext();
        patientNames = new JSONService() {
        }.getPatientNames(myContext);

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
        userInputText = (TextView) findViewById(R.id.userInputText);
    }

    @Override
    public void onResume() {
        super.onResume();
        patientNames = new JSONService() {
        }.getPatientNames(myContext);

        setContentView(R.layout.activity_splash__screen);

        myContext = getApplicationContext();

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
        userInputText = (TextView) findViewById(R.id.userInputText);
    }

    public void editPlans(View view) {
        if (!nameToPass.equals("Add New Patient")) {
            Intent intent = new Intent(this, View_Plans.class);
            intent.putExtra("patientName", nameToPass);
            intent.putExtra("isNew", false);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Enter new patient's name.");
            builder.setTitle("New Patient Name");

            final EditText passMe = new EditText(this);
            passMe.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(passMe);

            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            nameToPass = passMe.getText().toString();
                            Intent intent = new Intent(myContext, View_Plans.class);
                            intent.putExtra("patientName", nameToPass);
                            intent.putExtra("isNew", true);
                            startActivity(intent);
                        }
                    });

            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.show();
            if (!nameToPass.equals("Add New Patient")) {
                Intent intent = new Intent(this, View_Plans.class);
                intent.putExtra("patientName", nameToPass);
                startActivity(intent);
            }
        }
    }
}
