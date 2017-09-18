package com.ad_revenue.damp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ad_revenue.damp.Services.JSONService;

public class View_Plans extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context myContext = getApplicationContext();
        JSONService myJSON = new JSONService();
        String[] stuff = myJSON.getProperties(myContext, "plans.json", "Name");

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
        intent.putExtra("indexInto", position);
        startActivity(intent);
    }
}
