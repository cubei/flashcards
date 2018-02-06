package com.quchen.flashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FolderAdapter folderAdapter;

    private List<String> getFolders() {
        List<String> folders = new ArrayList<>();

        folders.add("Test0");
        folders.add("Test1");
        folders.add("Test2");
        folders.add("Test3");
        folders.add("Test4");
        folders.add("Test5");
        folders.add("Test6");
        folders.add("Test7");
        folders.add("Test8");
        folders.add("Test9");

        return folders;
    }

    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        String folder = folderAdapter.getItem(position);

        Intent intent = new Intent("com.quchen.flashcard.ListActivity");
        intent.putExtra(ListActivity.KEY_FOLDER, folder);
        startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        folderAdapter = new FolderAdapter(this);
        folderAdapter.addAll(getFolders());

        ListView listView = findViewById(R.id.folderList);
        listView.setAdapter(folderAdapter);

        listView.setOnItemClickListener(clickListener);
    }
}
