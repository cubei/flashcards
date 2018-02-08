package com.quchen.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    public static final String KEY_FOLDER = "folder";
    public static final String KEY_FILE = "file";

    private String folderName;

    private List<ListItem> getLists() {
        List<ListItem> lists = new ArrayList<>();

        lists.add(new ListItem("List0"));
        lists.add(new ListItem("List1"));
        lists.add(new ListItem("List2"));
        lists.add(new ListItem("List3"));
        lists.add(new ListItem("List4"));
        lists.add(new ListItem("List5"));
        lists.add(new ListItem("List6"));
        lists.add(new ListItem("List7"));
        lists.add(new ListItem("List8"));
        lists.add(new ListItem("List9"));

        return lists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        ListView listView = findViewById(R.id.listList);

        ListAdapter listAdapter = new ListAdapter(this, listView);
        listAdapter.addAll(getLists());

        listView.setAdapter(listAdapter);

        folderName = getIntent().getExtras().getString(KEY_FOLDER);

        TextView title = findViewById(R.id.listsTitle);
        title.setText(folderName);
    }

}
