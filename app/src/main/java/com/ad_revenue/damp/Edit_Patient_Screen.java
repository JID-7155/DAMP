package com.ad_revenue.damp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ad_revenue.damp.Services.JSONService;

public class Edit_Patient_Screen extends AppCompatActivity {
    JSONService jsonService;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonService = new JSONService();
        context = getApplicationContext();
        setContentView(R.layout.activity_edit__patient__screen);


    }

    public void savePatientInformation(View view) {
        View nameForm = findViewById(R.id.nameForm);
        View ageForm = findViewById(R.id.ageForm);
        View miscForm = findViewById(R.id.miscForm);

        EditText nameText = (EditText) nameForm.findViewById(R.id.nameText);
        EditText ageText = (EditText) ageForm.findViewById(R.id.ageText);
        EditText miscText = (EditText) miscForm.findViewById(R.id.miscText);

        jsonService.writeToPatients(this.context, getIntent().getStringExtra("patientName"),  nameText.getText().toString(), ageText.getText().toString(), miscText.getText().toString());
        Toast.makeText(context, "Patient Information saved successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }

}
