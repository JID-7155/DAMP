package com.ad_revenue.damp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import com.ad_revenue.damp.Services.JSONService;

public class Edit_Patient_Screen extends AppCompatActivity {
    JSONService jsonService;
    Context context;
    ArrayList<String> properties;
    String hospitalName;
    String hospitalAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonService = new JSONService();
        context = getApplicationContext();
        setContentView(R.layout.activity_edit__patient__screen);

        updateProperties();


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProperties();
    }

    public void savePatientInformation(View view) {
        View nameForm = findViewById(R.id.nameForm);
        View ageForm = findViewById(R.id.ageForm);
        View miscForm = findViewById(R.id.miscForm);
        View hospitalForm = findViewById(R.id.hospitalForm);

        EditText nameText = (EditText) nameForm.findViewById(R.id.nameText);
        EditText ageText = (EditText) ageForm.findViewById(R.id.ageText);
        EditText miscText = (EditText) miscForm.findViewById(R.id.miscText);
        EditText hospitalNameText = (EditText) hospitalForm.findViewById(R.id.hospitalNameText);
        EditText hospitalAddressText = (EditText) hospitalForm.findViewById(R.id.hospitalAddressText);

        jsonService.writeToPatients(this.context, getIntent().getStringExtra("patientName"),  nameText.getText().toString(), ageText.getText().toString(), miscText.getText().toString()
        , hospitalNameText.getText().toString(), hospitalAddressText.getText().toString());
        Toast.makeText(context, "Patient Information saved successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void goToMap(View view) {
        properties = jsonService.getPatientInformation(context, getIntent().getStringExtra("patientName"));
        Intent intent = new Intent(this, Map_Screen.class);
        intent.putExtra("patientProperties", properties);
        startActivity(intent);
    }

    public void updateProperties() {
        if (!jsonService.isPatientInfoPresent(context, getIntent().getStringExtra("patientName"))) {
            properties = new ArrayList<String>(){{
                add(0, getIntent().getStringExtra("patientName"));
                add(1, "");
                add(2, "");
                add(3, "");
                add(4, "");
            }};
        } else {
            properties = jsonService.getPatientInformation(context, getIntent().getStringExtra("patientName"));
        }
        hospitalName = properties.get(3);
        hospitalAddress = properties.get(4);

        View hospitalForm = findViewById(R.id.hospitalForm);

        EditText nameText = (EditText) findViewById(R.id.nameForm).findViewById(R.id.nameText);
        EditText ageText = (EditText) findViewById(R.id.ageForm).findViewById(R.id.ageText);
        EditText miscText = (EditText) findViewById(R.id.miscForm).findViewById(R.id.miscText);
        EditText hospitalNameText = (EditText) hospitalForm.findViewById(R.id.hospitalNameText);
        EditText hospitalAddressText = (EditText) hospitalForm.findViewById(R.id.hospitalAddressText);
        nameText.setText(properties.get(0));
        ageText.setText(properties.get(1));
        miscText.setText(properties.get(2));
        hospitalNameText.setText(properties.get(3));
        hospitalAddressText.setText(properties.get(4));
    }

}
