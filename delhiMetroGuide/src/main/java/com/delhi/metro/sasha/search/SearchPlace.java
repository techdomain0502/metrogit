package com.delhi.metro.sasha.search;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.googleapis.CustomExpandableListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class SearchPlace extends Activity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private final int TYPE_MONUMENT = 0;
    private final int TYPE_SHOPPING = 1;
    private final int TYPE_FOOD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_listview);

        ActionBar ab = getActionBar();
        if (ab != null) {
            ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new CustomExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String place ;
                Intent i = new Intent();
                String str[] =listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).split("\n");
                i.putExtra("place",str[0]);
                i.putExtra("type",groupPosition);
                if(groupPosition==2)
                  i.putExtra("attraction",str[1]);
                setResult(102, i);
                finish();
                return false;
            }
        });

    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Must See Monuments");
        listDataHeader.add("Top 8 places for Shopping!!");
        listDataHeader.add("Top 13 Street Foods Corners!!");

        // Adding child data

        String[] monument = getResources().getStringArray(R.array.monument);
        List<String> monumentList = new ArrayList<String>(Arrays.asList(monument));

        String[] shop = getResources().getStringArray(R.array.shop);
        List<String> shopList = new ArrayList<String>(Arrays.asList(shop));

        String[] streetfood = getResources().getStringArray(R.array.streetfood);
        List<String> streetfoodList = new ArrayList<String>(Arrays.asList(streetfood));


        listDataChild.put(listDataHeader.get(0), monumentList); // Header, Child data
        listDataChild.put(listDataHeader.get(1), shopList);
        listDataChild.put(listDataHeader.get(2), streetfoodList);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
