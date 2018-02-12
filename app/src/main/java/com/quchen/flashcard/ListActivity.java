package com.quchen.flashcard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    public static final String KEY_FOLDER = "folder";

    private class ListAdapter extends ArrayAdapter<ListFileItem> {

        private final ListView listView;
        private Button startBtn;
        private List<ListFileItem> listOfSelectedItems = new ArrayList<>();

        private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ListFileItem listItem = getItem(position);
                startListFile(listItem.getFilePath());
            }
        };

        private View.OnClickListener watchBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View parentRow = (View) view.getParent().getParent();
                final int position = listView.getPositionForView(parentRow);
                final ListFileItem listFileItem = getItem(position);
                startListView(listFileItem);
            }
        };

        private CompoundButton.OnCheckedChangeListener selectionChanged = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                final View parentRow = (View) compoundButton.getParent().getParent();
                final int position = listView.getPositionForView(parentRow);
                ListFileItem listItem = getItem(position);

                if(checked) {
                    listOfSelectedItems.add(listItem);
                } else {
                    listOfSelectedItems.remove(listItem);
                }

                changeStartBtnText(listOfSelectedItems.size() > 0);
            }
        };

        public List<ListFileItem> getListOfSelectedItems() {
            return listOfSelectedItems;
        }


        public ListAdapter(Context context, ListView listView) {
            super(context, 0);

            this.listView = listView;

            listView.setOnItemClickListener(itemClickListener);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListFileItem item = this.getItem(position);

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_view, parent, false);
            }

            TextView label = convertView.findViewById(R.id.label);
            label.setText(item.getlabel());

            ImageButton watchBtn = convertView.findViewById(R.id.showListBtn);
            watchBtn.setOnClickListener(watchBtnClickListener);

            CheckBox checkBox = convertView.findViewById(R.id.selectList);
            checkBox.setOnCheckedChangeListener(selectionChanged);

            return convertView;
        }
    }

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
        Intent intent = new Intent("com.quchen.flashcard.GameActivity");
        intent.putExtra(GameActivity.KEY_FILE_LIST, files);
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
