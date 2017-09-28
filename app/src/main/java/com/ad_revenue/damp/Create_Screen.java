package com.ad_revenue.damp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ad_revenue.damp.Services.JSONService;

public class Create_Screen extends AppCompatActivity {

    private JSONService myJSON;
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__screen);

        myContext = getApplicationContext();
        myJSON = new JSONService();

        if(getIntent().getBooleanExtra("template", false)) {

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


    }

    public void submitInformation(View view){
        EditText planName = (EditText) findViewById(R.id.editPlanName);
        String name = planName.getText().toString();

        EditText planSteps = (EditText) findViewById(R.id.editSteps);
        String steps = planSteps.getText().toString();

        EditText planMeds = (EditText) findViewById(R.id.editMeds);
        String meds = planMeds.getText().toString();

        EditText planOther = (EditText) findViewById(R.id.editOther);
        String other = planOther.getText().toString();

        myJSON.writeToPlans(myContext, name, steps, meds, other);

        Toast.makeText(myContext, "New Plan Created.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
