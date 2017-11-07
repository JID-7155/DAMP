package com.ad_revenue.damp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ad_revenue.damp.Services.JSONService;

public class Create_Screen extends AppCompatActivity {

    private JSONService myJSON;
    private Context myContext;
    private boolean editMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__screen);

        myContext = getApplicationContext();
        myJSON = new JSONService();

        if (getIntent().getBooleanExtra("template", false)) {

            int planNumber = getIntent().getIntExtra("indexInto", 0);
            String[] plan = myJSON.getAllProperties(myContext, planNumber);

            EditText planName = (EditText) findViewById(R.id.editPlanName);
            planName.setText(plan[0]);

            EditText planSteps = (EditText) findViewById(R.id.editSteps);
            planSteps.setText(plan[1]);

            EditText planMeds = (EditText) findViewById(R.id.editMeds);
            planMeds.setText(plan[2]);

            EditText planOther = (EditText) findViewById(R.id.editOther);
            planOther.setText(plan[3]);
        }

        if (getIntent().getBooleanExtra("edit", false)) {
            editMode = true;

            String[] plan = new String[4];
            int planNumber = getIntent().getIntExtra("indexInto", 0);
            String patientName = getIntent().getStringExtra("patientName");

            plan[0] = myJSON.getInternalPlanProperties(myContext, patientName, "Name")[planNumber];
            plan[1] = myJSON.getInternalPlanProperties(myContext, patientName, "Steps")[planNumber];
            plan[2] = myJSON.getInternalPlanProperties(myContext, patientName, "Medications")[planNumber];
            plan[3] = myJSON.getInternalPlanProperties(myContext, patientName, "Other Notes")[planNumber];

            EditText planName = (EditText) findViewById(R.id.editPlanName);
            planName.setText(plan[0]);

            EditText planSteps = (EditText) findViewById(R.id.editSteps);
            planSteps.setText(plan[1]);

            EditText planMeds = (EditText) findViewById(R.id.editMeds);
            planMeds.setText(plan[2]);

            EditText planOther = (EditText) findViewById(R.id.editOther);
            planOther.setText(plan[3]);
        } else {
            editMode = false;
        }

    }

    public void submitInformation(View view) {
        EditText planName = (EditText) findViewById(R.id.editPlanName);
        String name = planName.getText().toString();

        EditText planSteps = (EditText) findViewById(R.id.editSteps);
        String steps = planSteps.getText().toString();

        EditText planMeds = (EditText) findViewById(R.id.editMeds);
        String meds = planMeds.getText().toString();

        EditText planOther = (EditText) findViewById(R.id.editOther);
        String other = planOther.getText().toString();

        if (editMode) {
            myJSON.deletePlan(myContext, getIntent().getStringExtra("patientName"), getIntent().getIntExtra("indexInto", 0));
            myJSON.writeToPlans(myContext, getIntent().getStringExtra("patientName"), name, steps, meds, other);
            /*
            myJSON.editPlans(myContext, getIntent.getIntExtra("indexInto"), getIntent().getStringExtra("patientName"), name, steps, meds, other);
             */
            //onResume();
            Toast.makeText(myContext, "Plan Edited", Toast.LENGTH_SHORT).show();
        } else {
            myJSON.writeToPlans(myContext, getIntent().getStringExtra("patientName"), name, steps, meds, other);
            Toast.makeText(myContext, "New Plan Created.", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
