package com.quchen.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    public static final String KEY_FOLDER = "folder";

    private String folderName;
    private ListAdapter listAdapter;

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

    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        ListItem listItem = listAdapter.getItem(position);

        Intent intent = new Intent("com.quchen.flashcard.ListActivity");
        intent.putExtra(ListActivity.KEY_FOLDER, listItem.getlabel());
        startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        listAdapter = new ListAdapter(this);
        listAdapter.addAll(getLists());

        ListView listView = findViewById(R.id.listList);
        listView.setAdapter(listAdapter);

        folderName = getIntent().getExtras().getString(KEY_FOLDER);

        TextView title = findViewById(R.id.title);
        title.setText(folderName);
    }

}
