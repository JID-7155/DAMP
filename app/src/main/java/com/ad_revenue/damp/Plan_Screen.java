package com.ad_revenue.damp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ad_revenue.damp.Services.JSONService;

public class Plan_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan__screen);

        Context myContext = getApplicationContext();
        JSONService myJSON = new JSONService();

        String[] steps = myJSON.getInternalProperties(myContext, "plans.json", "Steps");
        int planNumber = getIntent().getIntExtra("indexInto", 0);
        String[] stringSteps = steps[planNumber].split("\\\\r?\\\\n");

        CustomAdapter adapter = new CustomAdapter(this, stringSteps);
        ListView listView = (ListView) findViewById(R.id.stepList);
        listView.setAdapter(adapter);

        TextView planTitle = (TextView) findViewById(R.id.planNameText);
        planTitle.setText(myJSON.getInternalProperties(myContext, "plans.json", "Name")[planNumber]);
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
