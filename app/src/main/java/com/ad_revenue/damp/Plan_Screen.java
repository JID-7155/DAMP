package com.ad_revenue.damp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ad_revenue.damp.Services.JSONService;

public class Plan_Screen extends AppCompatActivity {

    private boolean edited;
    private Context myContext;
    private String patientName;
    private int planNumber;
    private JSONService myJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        edited = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan__screen);

        myContext = getApplicationContext();
        myJSON = new JSONService();
        patientName = getIntent().getStringExtra("patientName");

        String[] steps = myJSON.getInternalPlanProperties(myContext, patientName, "Steps");
        planNumber = getIntent().getIntExtra("indexInto", 0);
        String[] stringSteps = steps[planNumber].split("\\\\r?\\\\n");

        CustomAdapter adapter = new CustomAdapter(this, stringSteps);
        ListView listView = (ListView) findViewById(R.id.stepList);
        listView.setAdapter(adapter);

        TextView planTitle = (TextView) findViewById(R.id.planNameText);
        planTitle.setText(myJSON.getInternalPlanProperties(myContext, patientName, "Name")[planNumber]);
    }

    public void editCurrentPlan(View view) {
        edited = true;
        Intent intent = new Intent(this, Create_Screen.class);
        intent.putExtra("indexInto", getIntent().getIntExtra("indexInto", 0));
        intent.putExtra("patientName", getIntent().getStringExtra("patientName"));
        intent.putExtra("edit", true);
        startActivity(intent);
    }

    public void exportCurrentPlan(View view) {
        if(myJSON.sharePlan(myContext, patientName, planNumber)) {
            Toast.makeText(myContext, "Plan successfully exported.", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(myContext, "Plan export failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (edited) {
            finish();
        }
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        Context context;
        String[] title;


        CustomAdapter(Context c, String[] title) {

            super(c, R.layout.single_step, title);
            this.context = c;
            this.title = title;

        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = vi.inflate(R.layout.single_step, parent, false);
                TextView stepText = (TextView) row.findViewById(R.id.stepText);
                stepText.setText(title[position]);
                return row;
            }
            return convertView;
        }
    }
}
