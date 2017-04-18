package com.ad_revenue.damp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Plan_Screen extends AppCompatActivity {

    String[] steps = {"Call emergency medical services.", "Carefully remove arrow.", "Utilize cloth or bandage, bind wound.",
            "Apply pressure to bandaged wound.", "Hydrate wounded individual.", "When bleeding stops, clean wound to prevent infection."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan__screen);

        CustomAdapter adapter = new CustomAdapter(this,steps);
        ListView listView = (ListView) findViewById(R.id.stepList);
        listView.setAdapter(adapter);
    }

    class CustomAdapter extends ArrayAdapter<String>
    {
        Context context;
        String[] title;


        CustomAdapter(Context c, String[] title)
        {

            super(c, R.layout.single_step,title);
            this.context = c;
            this.title=title;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = vi.inflate(R.layout.single_step, parent, false);
            TextView titlee = (TextView) row.findViewById(R.id.stepText);
            int pos = position+1;
            titlee.setText(+pos + ". " + title[position]);
            pos++;
            return row;
        }

    }
}
