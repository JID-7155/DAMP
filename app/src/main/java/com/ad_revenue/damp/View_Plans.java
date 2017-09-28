package com.ad_revenue.damp;

import android.app.ListActivity;
import android.content.Context;
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

    private enum ListMode {EDIT, DELETE}
    private ListMode currentMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentMode = ListMode.EDIT;
        myContext = getApplicationContext();
        myJSON = new JSONService();

        setupExamplePlans(myContext, myJSON);
        String[] stuff = myJSON.getInternalProperties(myContext, "plans.json", "Name");

        setContentView(R.layout.activity_view__plans);
        ArrayAdapter adapter = new ArrayAdapter<>(this,R.layout.activity_singleplan,stuff);
        ListView listView = this.getListView();
        listView.setAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);


        switch(currentMode) {
            case EDIT:
                gotoPlan(position);
                break;
            case DELETE:
                deletePlan(position);
                break;

        }

    }

    public void gotoPlan(int position) {

        Intent intent = new Intent(this, Plan_Screen.class);
        intent.putExtra("indexInto", position);
        startActivity(intent);
    }

    public void gotoDeleteMode(View v) {
        if(currentMode != ListMode.DELETE) {
            currentMode = ListMode.DELETE;
        } else {
            currentMode = ListMode.EDIT;
        }
    }

    public void deletePlan(int position) {
        myJSON.deleteSection(myContext, position);
        onResume();
    }


    public void addPlan(View view) {
        Intent intent = new Intent(this, Create_Screen.class);
        startActivity(intent);
    }

    protected void setupExamplePlans(Context context, JSONService myJ) {
        myJ.writeToPlans(context, "Example Plan 1", "1. Brace your core.\n\n2. Squat.", "Aspirin", "Source: Adrian.");
        myJ.writeToPlans(context, "Example Plan 2", "1. Brace your core.\n\n2. Lift.", "Aspirin", "Source: Adrian.");
        myJ.writeToPlans(context, "Example Plan 3", "1. Brace your core.\n\n2. Press.", "Aspirin", "Source: Adrian.");
        Toast.makeText(context, "Plans loaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();

        String[] stuff = myJSON.getInternalProperties(myContext, "plans.json", "Name");

        setContentView(R.layout.activity_view__plans);
        ArrayAdapter adapter = new ArrayAdapter<>(this,R.layout.activity_singleplan,stuff);
        ListView listView = this.getListView();
        listView.setAdapter(adapter);
    }
}
