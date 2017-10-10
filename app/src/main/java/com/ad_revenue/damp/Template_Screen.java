package com.ad_revenue.damp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ad_revenue.damp.Services.JSONService;

public class Template_Screen extends ListActivity {

    private boolean template;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context myContext = getApplicationContext();
        JSONService myJSON = new JSONService();

        String[] conditions = myJSON.getProperties(myContext, "conditions.json", "Name");

        setContentView(R.layout.activity_template__screen);
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_singleplan, conditions);
        ListView listView = this.getListView();
        listView.setAdapter(adapter);

        template = false;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(this, Create_Screen.class);
        intent.putExtra("indexInto", position);

        template = true;
        intent.putExtra("template", true);
        intent.putExtra("patientName", getIntent().getStringExtra("patientName"));

        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (template) {
            finish();
        }
    }
}
