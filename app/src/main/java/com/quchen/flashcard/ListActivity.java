package com.quchen.flashcard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {

    public static final String KEY_FOLDER = "folder";

    private static final int STORAGE_READ_PERMISSOIN_REQUEST_ID = 42;
    private static final int GET_FILE_REQUEST_ID = 1337;

    private class ListAdapter extends ArrayAdapter<ListFileItem> {

        private final ListView listView;
        private final List<ListFileItem> listOfSelectedItems = new ArrayList<>();


        private final View.OnClickListener watchBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View parentRow = (View) view.getParent().getParent();
                final int position = listView.getPositionForView(parentRow);
                final ListFileItem listFileItem = getItem(position);
                startListView(listFileItem);
            }
        };

        private final CompoundButton.OnCheckedChangeListener selectionChanged = new CompoundButton.OnCheckedChangeListener() {
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

        private List<ListFileItem> getListOfSelectedItems() {
            return listOfSelectedItems;
        }


        private ListAdapter(Context context, ListView listView) {
            super(context, 0);

            this.listView = listView;

            listView.setOnItemClickListener((adapterView, view, position, l) -> {
                ListFileItem listItem = getItem(position);
                startGameActivity(listItem.getFilePath());
            });
            listView.setOnItemLongClickListener((adapterView, view, position, l) -> {
                ListFileItem listItem = getItem(position);
                final File file = new File(App.getListRootDir(), listItem.getFilePath());

                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setMessage(String.format("%s %s", getResources().getString(R.string.deleteList), listItem.getLabel()))
                        .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                            if (file.delete()) {
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .show();

                return true;
            });
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListFileItem item = this.getItem(position);

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_view, parent, false);
            }

            TextView label = convertView.findViewById(R.id.label);
            label.setText(item.getLabel());

            ImageButton watchBtn = convertView.findViewById(R.id.showListBtn);
            watchBtn.setOnClickListener(watchBtnClickListener);

            CheckBox checkBox = convertView.findViewById(R.id.selectList);
            checkBox.setOnCheckedChangeListener(null); // Shortly remove listener to set the state without callback
            checkBox.setChecked(listOfSelectedItems.contains(item));
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
            lists.add(new ListFileItem(folderName, listFile.getName()));
        }

        Collections.sort(lists, (obj1, obj2) -> obj1.getLabel().compareTo(obj2.getLabel()));

        return lists;
    }

    public void startListView(ListFileItem listFileItem) {
        Intent intent = new Intent(this, ListViewActivity.class);
        intent.putExtra(ListViewActivity.KEY_FILE, listFileItem.getFilePath());
        startActivity(intent);
    }

    public void startGameActivity(String file) {
        String[] files = {file};
        startGameActivity(files);
    }

    public void startGameActivity(String[] files) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.KEY_FILE_LIST, files);
        startActivity(intent);
    }

    private final View.OnClickListener startBtnOnClick = new View.OnClickListener() {
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

            if (fileList.size() > 0) {
                startGameActivity(fileList.toArray(new String[0]));
            } else {
                Toast.makeText(ListActivity.this, R.string.addFileAlert, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void showListImport() {
        Intent intent = new Intent()
                .setType("text/comma-separated-values")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), GET_FILE_REQUEST_ID);
    }

    private void showListAdd() {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            View view = this.getLayoutInflater().inflate(R.layout.add_list_view, null);
            alert.setView(view);
            final EditText leftEdit = view.findViewById(R.id.leftEdit);
            final EditText rightEdit = view.findViewById(R.id.rightEdit);
            final EditText listNameEdit = view.findViewById(R.id.title);

            alert.setPositiveButton(R.string.btn_addList, (dialog, id) -> {
                final String leftVal = leftEdit.getText().toString();
                final String rightVal = rightEdit.getText().toString();
                final String titleVal = listNameEdit.getText().toString();

                if(leftVal.isEmpty() || rightVal.isEmpty() || titleVal.isEmpty())
                {
                    Toast.makeText(ListActivity.this, R.string.addListError, Toast.LENGTH_SHORT).show();
                    return;
                }

                ListItem listItem = new ListItem(String.format("%s/%s.csv", folderName, titleVal), leftVal, rightVal);
                listItem.saveToFile();

                finish();
                startActivity(getIntent());
            });

            alert.setNegativeButton(R.string.createFolderNoOption, (dialog, id) -> {

            });

            alert.show();
    }

    // https://stackoverflow.com/a/25005243
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = Math.max(0, cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    result = cursor.getString(index);
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private final View.OnClickListener importListBtnOnClick = view -> showListImport();

    private final View.OnClickListener addListBtnOnClick = view -> showListAdd();

    private boolean copyFileFromUri(Uri fileUri, String fileName) {
        boolean success = true;

        File folder = new File(App.getListRootDir(), folderName);
        File file = new File(folder, fileName);

        try {
            InputStream in = getContentResolver().openInputStream(fileUri);
            OutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while((len = in.read(buffer, 0, buffer.length)) != -1){
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_FILE_REQUEST_ID && resultCode == RESULT_OK) {
            Uri selectedFile = data.getData();
            String fileName = getFileName(selectedFile);
            String fileExtension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
            if(fileExtension.equalsIgnoreCase("csv") && copyFileFromUri(selectedFile, fileName)) {
                listAdapter.add(new ListFileItem(folderName, fileName));
                finish();
                startActivity(getIntent());
            } else {
                Toast.makeText(this, R.string.listImportFileError, Toast.LENGTH_LONG).show();
            }
        }
    }

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

        folderName = getIntent().getExtras().getString(KEY_FOLDER);

        ListView listView = findViewById(R.id.listList);

        listAdapter = new ListAdapter(this, listView);
        listAdapter.addAll(getLists());

        listView.setAdapter(listAdapter);

        TextView title = findViewById(R.id.listsTitle);
        title.setText(folderName);

        startButton = findViewById(R.id.startBtn);
        startButton.setOnClickListener(startBtnOnClick);

        Button importButton = findViewById(R.id.importListBtn);
        importButton.setOnClickListener(importListBtnOnClick);

        Button addButton = findViewById(R.id.addListBtn);
        addButton.setOnClickListener(addListBtnOnClick);
    }

}
