package com.ad_revenue.damp;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class View_Plans extends AppCompatActivity {

    String[] stuff = {"Rick's Renal Dialysis Plan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__plans);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_singleplan,stuff);
        //System.out.println("yah boy");
        ListView listView = (ListView) findViewById(R.id.viewPlansList);
        listView.setAdapter(adapter);






    }
}
