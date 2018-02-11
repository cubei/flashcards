package com.quchen.flashcard;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FolderAdapter folderAdapter;

    private List<String> getFolders() {
        List<String> folders = new ArrayList<>();

        File listRoodDir = App.getListRootDir();

        for(File listFolder: listRoodDir.listFiles()) {
            folders.add(listFolder.getName());
        }

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateListFiles() {
        copyFileFromResource(R.raw.artist_title, "Music", "artist_title.csv");
        copyFileFromResource(R.raw.lyrics, "Music", "lyrics.csv");
        copyFileFromResource(R.raw.quotes, "TV","quotes.csv");
        copyFileFromResource(R.raw.basic_chinese, "Chinese","basic.csv");
        copyFileFromResource(R.raw.numbers_chinese, "Chinese","numbers.csv");
        copyFileFromResource(R.raw.basic_german, "German","basic.csv");
        copyFileFromResource(R.raw.numbers_german, "German","numbers.csv");
        copyFileFromResource(R.raw.basic_japanese, "Japanese","basic.csv");
        copyFileFromResource(R.raw.numbers_japanese, "Japanese","numbers.csv");
        copyFileFromResource(R.raw.basic_spanish, "Spanish","basic.csv");
        copyFileFromResource(R.raw.numbers_spanish, "Spanish","numbers.csv");
        copyFileFromResource(R.raw.hiragana, "Japanese","hiragana.csv");
        copyFileFromResource(R.raw.katakana, "Japanese","katakana.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade1, "Japanese","Kanji1.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade2, "Japanese","Kanji2.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade3, "Japanese","Kanji3.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade4, "Japanese","Kanji4.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade5, "Japanese","Kanji5.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade6, "Japanese","Kanji6.csv");
        copyFileFromResource(R.raw.japanese_lektion0, "Minna no Nihongo","Lektion0.csv");
        copyFileFromResource(R.raw.japanese_lektion1, "Minna no Nihongo","Lektion1.csv");
        copyFileFromResource(R.raw.japanese_lektion2, "Minna no Nihongo","Lektion2.csv");
        copyFileFromResource(R.raw.japanese_lektion3, "Minna no Nihongo","Lektion3.csv");
        copyFileFromResource(R.raw.japanese_lektion4, "Minna no Nihongo","Lektion4.csv");
        copyFileFromResource(R.raw.japanese_lektion5, "Minna no Nihongo","Lektion5.csv");
        copyFileFromResource(R.raw.japanese_particle, "Minna no Nihongo","Particle.csv");
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
