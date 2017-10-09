package com.ad_revenue.damp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ad_revenue.damp.Services.JSONService;

public class View_Plans extends ListActivity {

    private JSONService myJSON;
    private Context myContext;
    private ListMode currentMode;
    private String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentMode = ListMode.EDIT;
        myContext = getApplicationContext();
        myJSON = new JSONService();
        patientName = getIntent().getStringExtra("patientName");

        setupExamplePlans(myContext, myJSON);
        String[] plans = myJSON.getInternalPlanProperties(myContext, patientName, "Name");

        setContentView(R.layout.activity_view__plans);
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_singleplan, plans);
        ListView listView = this.getListView();
        listView.setAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        switch (currentMode) {
            case EDIT:
                goToPlan(position);
                break;
            case DELETE:
                deletePlan(position);
                break;
        }
    }

    public void goToPlan(int position) {
        Intent intent = new Intent(this, Plan_Screen.class);
        intent.putExtra("indexInto", position);
        intent.putExtra("patientName", patientName);
        startActivity(intent);
    }

    public void switchMode(View v) {
        if (currentMode != ListMode.DELETE) {
            currentMode = ListMode.DELETE;
            Toast.makeText(myContext, "Delete Mode", Toast.LENGTH_SHORT).show();
        } else {
            currentMode = ListMode.EDIT;
            Toast.makeText(myContext, "Edit/View Mode", Toast.LENGTH_SHORT).show();
        }
    }

    public void deletePlan(int position) {
        myJSON.deletePlan(myContext, patientName, position);
        Toast.makeText(myContext, "Plan deleted.", Toast.LENGTH_SHORT).show();
        onResume();
    }

    public void addPlan(View view) {
        AlertDialog choice = new AlertDialog.Builder(this).create();

        choice.setTitle("Add Plan");
        choice.setMessage("Do you want to start with a blank plan or a template?");

        choice.setButton(AlertDialog.BUTTON_POSITIVE, "Blank",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), Create_Screen.class);
                        startActivity(intent);
                    }
                });

        choice.setButton(AlertDialog.BUTTON_NEGATIVE, "Template",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), Template_Screen.class);
                        startActivity(intent);
                    }
                });

        choice.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        choice.show();

    }

    protected void setupExamplePlans(Context context, JSONService myJ) {
        myJ.writeToPlans(context, patientName, "Example Plan 1", "1. Brace your core.\n\n2. Squat.", "Aspirin", "Source: Adrian.");
        myJ.writeToPlans(context, patientName, "Example Plan 2", "1. Brace your core.\n\n2. Lift.", "Aspirin", "Source: Adrian.");
        myJ.writeToPlans(context, patientName, "Example Plan 3", "1. Brace your core.\n\n2. Press.", "Aspirin", "Source: Adrian.");
        Toast.makeText(context, "Plans loaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        String[] plans = myJSON.getInternalPlanProperties(myContext, patientName, "Name");

        setContentView(R.layout.activity_view__plans);
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_singleplan, plans);
        ListView listView = this.getListView();
        listView.setAdapter(adapter);
    }

    private enum ListMode {EDIT, DELETE}
}
