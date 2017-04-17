package com.ad_revenue.damp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class View_Plans extends ListActivity {

    String[] stuff = {"Rick's Renal Dialysis Plan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__plans);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_singeplan,stuff);
        ListView listView = this.getListView();
        listView.setAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(this, Plan_Screen.class);
        startActivity(intent);
    }
}
