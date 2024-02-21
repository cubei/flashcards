package com.quchen.flashcard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static class FolderAdapter extends ArrayAdapter<String> {

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

        alert.setPositiveButton(R.string.createFolderYesOption, (dialog, id) -> {
            String folderName = edittext.getText().toString();
            File folder = new File(App.getListRootDir(), folderName);
            if(folder.mkdir()) {
                folderAdapter.add(folderName);
            } else {
                Toast.makeText(MainActivity.this, R.string.folderCreateError, Toast.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton(R.string.createFolderNoOption, null);

        alert.show();
    }

    public void addFolderOnClick(View view) {
        showFolderCreateDialog();
    }

    private final AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            String folder = folderAdapter.getItem(position);

            Intent intent = new Intent(MainActivity.this, ListActivity.class);
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
        File folder_Chinese = new File(App.getListRootDir(), "Chinese");
        new File(folder_Chinese, "basic_extented.csv").delete();
    }

    private void updateListFiles() {
        copyFileFromResource(R.raw.japanese_lektion0, "Minna no Nihongo","Lektion 0.csv");
        copyFileFromResource(R.raw.japanese_lektion1, "Minna no Nihongo","Lektion 1.csv");
        copyFileFromResource(R.raw.japanese_lektion2, "Minna no Nihongo","Lektion 2.csv");
        copyFileFromResource(R.raw.japanese_lektion3, "Minna no Nihongo","Lektion 3.csv");
        copyFileFromResource(R.raw.japanese_lektion4, "Minna no Nihongo","Lektion 4.csv");
        copyFileFromResource(R.raw.japanese_lektion5, "Minna no Nihongo","Lektion 5.csv");
        copyFileFromResource(R.raw.japanese_lektion6, "Minna no Nihongo","Lektion 6.csv");
        copyFileFromResource(R.raw.japanese_lektion6_lebensmittel, "Minna no Nihongo","Lektion 6 Lebensmittel.csv");
        copyFileFromResource(R.raw.japanese_lektion7, "Minna no Nihongo","Lektion 7.csv");
        copyFileFromResource(R.raw.japanese_lektion7_familie, "Minna no Nihongo","Lektion 7 Familie.csv");
        copyFileFromResource(R.raw.japanese_lektion8, "Minna no Nihongo","Lektion 8.csv");
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
        copyFileFromResource(R.raw.animals_japanese_emoji, "Japanese","Animal Emoji.csv");
        copyFileFromResource(R.raw.fruit_japanese_emoji, "Japanese","Fruit Emoji.csv");
        copyFileFromResource(R.raw.colours_japanese_emoji, "Japanese","Colours Emoji.csv");

        copyFileFromResource(R.raw.basic_chinese, "Chinese","basic.csv");
        copyFileFromResource(R.raw.basic_chinese_introduction, "Chinese","introduction.csv");
        copyFileFromResource(R.raw.basic_chinese_family, "Chinese","family.csv");
        copyFileFromResource(R.raw.basic_chinese_extended, "Chinese","basic_extended");
        copyFileFromResource(R.raw.numbers_chinese, "Chinese","numbers.csv");
        copyFileFromResource(R.raw.colours_chinese, "Chinese","colours.csv");
        copyFileFromResource(R.raw.japanese_kanji_grade1_pinyin, "Chinese","characters 1.csv");
        copyFileFromResource(R.raw.animals_chinese_emoji, "Chinese","Animal Emoji.csv");
        copyFileFromResource(R.raw.fruit_chinese_emoji, "Chinese","Fruit Emoji.csv");
        copyFileFromResource(R.raw.colours_chinese_emoji, "Chinese","Colours Emoji.csv");

        copyFileFromResource(R.raw.basic_german, "German","basic.csv");
        copyFileFromResource(R.raw.numbers_german, "German","numbers.csv");
        copyFileFromResource(R.raw.animals_german_emoji, "German","Tiere Emoji.csv");
        copyFileFromResource(R.raw.fruit_german_emoji, "German","Früchte Emoji.csv");
        copyFileFromResource(R.raw.colours_german_emoji, "German","Farben Emoji.csv");

        copyFileFromResource(R.raw.animals_english_emoji, "English","Animal Emoji.csv");
        copyFileFromResource(R.raw.fruit_english_emoji, "English","Fruit Emoji.csv");
        copyFileFromResource(R.raw.colours_english_emoji, "English","Colours Emoji.csv");

        copyFileFromResource(R.raw.basic_spanish, "Spanish","basic.csv");
        copyFileFromResource(R.raw.numbers_spanish, "Spanish","numbers.csv");

        copyFileFromResource(R.raw.animals_turkish_emoji, "Turkish","animals.csv");
        copyFileFromResource(R.raw.fruit_turkish_emoji, "Turkish","fruits.csv");

        cleanupOldFiles();
    }


    private final AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
            final String folder = folderAdapter.getItem(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(String.format("%s %s", getResources().getString(R.string.deleteFolder), folder))
                    .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                        if(App.deleteDirectory(new File(App.getListRootDir(), folder))) {
                            finish();
                            startActivity(getIntent());
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), null)
                    .show();

            return true;
        }
    };

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
        listView.setOnItemLongClickListener(itemLongClickListener);
    }
}
