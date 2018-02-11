package com.quchen.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    public static final String KEY_FOLDER = "folder";
    public static final String KEY_FILE_LIST = "file";

    private String folderName;
    private ListAdapter listAdapter;
    private Button startButton;

    private List<ListFileItem> getLists() {
        List<ListFileItem> lists = new ArrayList<>();

        File listRoodDir = App.getListRootDir();
        File listFolder = new File(listRoodDir, folderName);

        for(File listFile: listFolder.listFiles()) {
            lists.add(new ListFileItem(folderName,listFile.getName()));
        }

        return lists;
    }

    public void startListView(ListFileItem listFileItem) {
        Intent intent = new Intent("com.quchen.flashcard.ListViewActivity");
        intent.putExtra(ListViewActivity.KEY_FILE, listFileItem.getFilePath());
        startActivity(intent);
    }

    public void startListFile(String file) {
        String files[] = {file};
        startListFiles(files);
    }

    public void startListFiles(String files[]) {
        Intent intent = new Intent("com.quchen.flashcard.ListCfgActivity");
        intent.putExtra(KEY_FILE_LIST, files);
        startActivity(intent);
    }

    private View.OnClickListener startBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            List<String> fileList = new ArrayList<>();

            List<ListFileItem> listItems = listAdapter.getListOfSelectedItems();

            if(listItems.size() > 0) {
                // Add checked lists
                for(ListFileItem li: listItems) {
                    fileList.add(li.getFilePath());
                }
            } else {
                // Add all lists
                for(int i = 0; i < listAdapter.getCount(); i++) {
                    fileList.add(listAdapter.getItem(i).getFilePath());
                }
            }

            startListFiles(fileList.toArray(new String[fileList.size()]));
        }
    };

    public void changeStartBtnText(boolean isSelection) {
        if(isSelection) {
            startButton.setText(R.string.btn_startSelection);
        } else {
            startButton.setText(R.string.btn_startAll);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        ListView listView = findViewById(R.id.listList);

        folderName = getIntent().getExtras().getString(KEY_FOLDER);

        listAdapter = new ListAdapter(this, listView);
        listAdapter.addAll(getLists());

        listView.setAdapter(listAdapter);

        TextView title = findViewById(R.id.listsTitle);
        title.setText(folderName);

        startButton = findViewById(R.id.startBtn);
        startButton.setOnClickListener(startBtnOnClick);

//        Button importButton = findViewById(R.id.importListBtn);
    }

}
