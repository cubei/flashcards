package com.quchen.flashcard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private class FolderAdapter extends ArrayAdapter<String> {

        public FolderAdapter(Context context) {
            super(context, 0);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String item = this.getItem(position);

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.folder_item_view, parent, false);
            }

            TextView label = convertView.findViewById(R.id.label);
            label.setText(item);

            return convertView;
        }
    }

    private FolderAdapter folderAdapter;

    private List<String> getFolders() {
        List<String> folders = new ArrayList<>();

        File listRoodDir = App.getListRootDir();

        for(File listFolder: listRoodDir.listFiles()) {
            folders.add(listFolder.getName());
        }

        Collections.sort(folders);

        return folders;
    }

    private void showFolderCreateDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);
        alert.setTitle(R.string.createFolderDialogTitle);
        alert.setMessage(R.string.createFolderDialogMessage);

        alert.setView(edittext);

        alert.setPositiveButton(R.string.createFolderYesOption, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String folderName = edittext.getText().toString();
                File folder = new File(App.getListRootDir(), folderName);
                folder.mkdir();
                folderAdapter.add(folderName);
            }
        });

        alert.setNegativeButton(R.string.createFolderNoOption, null);

        alert.show();
    }

    public void addFolderOnClick(View view) {
        showFolderCreateDialog();
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

    private void copyFileFromResource(int id, String folderName, String fileName) {
        InputStream in = getResources().openRawResource(id);
        File folder = new File(App.getListRootDir(), folderName);
        folder.mkdirs();
        File file = new File(folder, fileName);

        try {
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
        }
    }

    /** Renamed some files, so the old ones should be deleted */
    private void cleanupOldFiles() {
        File folder_minnaNoNihongo = new File(App.getListRootDir(), "Minna no Nihongo");
        new File(folder_minnaNoNihongo, "Lektion0.csv").delete();
        new File(folder_minnaNoNihongo, "Lektion1.csv").delete();
        new File(folder_minnaNoNihongo, "Lektion2.csv").delete();
        new File(folder_minnaNoNihongo, "Lektion3.csv").delete();
        new File(folder_minnaNoNihongo, "Lektion4.csv").delete();
        new File(folder_minnaNoNihongo, "Lektion5.csv").delete();

        File folder_Japanese = new File(App.getListRootDir(), "Japanese");
        new File(folder_Japanese, "Kanji1.csv").delete();
        new File(folder_Japanese, "Kanji2.csv").delete();
        new File(folder_Japanese, "Kanji3.csv").delete();
        new File(folder_Japanese, "Kanji4.csv").delete();
        new File(folder_Japanese, "Kanji5.csv").delete();
        new File(folder_Japanese, "Kanji6.csv").delete();

        File folder_Chinese = new File(App.getListRootDir(), "Chinese");
        new File(folder_Chinese, "characters1.csv").delete();
    }

    private void updateListFiles() {
        copyFileFromResource(R.raw.japanese_lektion0, "Minna no Nihongo","Lektion 0.csv");
        copyFileFromResource(R.raw.japanese_lektion1, "Minna no Nihongo","Lektion 1.csv");
        copyFileFromResource(R.raw.japanese_lektion2, "Minna no Nihongo","Lektion 2.csv");
        copyFileFromResource(R.raw.japanese_lektion3, "Minna no Nihongo","Lektion 3.csv");
        copyFileFromResource(R.raw.japanese_lektion4, "Minna no Nihongo","Lektion 4.csv");
        copyFileFromResource(R.raw.japanese_lektion5, "Minna no Nihongo","Lektion 5.csv");
        copyFileFromResource(R.raw.japanese_lektion6, "Minna no Nihongo","Lektion 6.csv");
        copyFileFromResource(R.raw.japanese_lektion7, "Minna no Nihongo","Lektion 7.csv");
        copyFileFromResource(R.raw.japanese_particle, "Minna no Nihongo","Particle.csv");
        copyFileFromResource(R.raw.minna_no_nihongo_kanji, "Minna no Nihongo","Kanji.csv");

        copyFileFromResource(R.raw.hiragana, "Japanese","hiragana.csv");
        copyFileFromResource(R.raw.katakana, "Japanese","katakana.csv");
        copyFileFromResource(R.raw.basic_japanese, "Japanese","basic.csv");
        copyFileFromResource(R.raw.numbers_japanese, "Japanese","numbers.csv");
        copyFileFromResource(R.raw.japanese_dates, "Japanese","dates.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade1, "Japanese","Kanji 1.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade2, "Japanese","Kanji 2.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade3, "Japanese","Kanji 3.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade4, "Japanese","Kanji 4.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade5, "Japanese","Kanji 5.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade6, "Japanese","Kanji 6.csv");

        copyFileFromResource(R.raw.basic_chinese, "Chinese","basic.csv");
        copyFileFromResource(R.raw.numbers_chinese, "Chinese","numbers.csv");
        copyFileFromResource(R.raw.colours_chinese, "Chinese","colours.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade1_pinyin, "Chinese","characters 1.csv");

        copyFileFromResource(R.raw.basic_german, "German","basic.csv");
        copyFileFromResource(R.raw.numbers_german, "German","numbers.csv");

        copyFileFromResource(R.raw.basic_spanish, "Spanish","basic.csv");
        copyFileFromResource(R.raw.numbers_spanish, "Spanish","numbers.csv");

        cleanupOldFiles();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateListFiles();

        folderAdapter = new FolderAdapter(this);
        folderAdapter.addAll(getFolders());

        ListView listView = findViewById(R.id.folderList);
        listView.setAdapter(folderAdapter);

        listView.setOnItemClickListener(clickListener);
    }
}
