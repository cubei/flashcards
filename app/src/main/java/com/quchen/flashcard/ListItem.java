package com.quchen.flashcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lars on 09.02.2018.
 */

public class ListItem {

    static final int LEFT_IDX = 0;
    static final int RIGHT_IDX = 1;

    private String filePath;
    private List<String> header;
    private List<List<String>> items = null;

    public ListItem(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private static List<List<String>> getFileContent(File file) throws IOException {

        List<List<String>> fileContentList = new ArrayList<>();

        if(file.exists()) {

            InputStream inputStream = new FileInputStream(file);
            Reader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);

            String line;

            while ((line = br.readLine()) != null) {
                if(!line.equals("")) {
                    ArrayList<String> values = new ArrayList<>(Arrays.asList(line.split(";")));
                    fileContentList.add(values);
                }
            }

            br.close();
        }

        return fileContentList;
    }

    private void readFile()
    {
        File file = new File(App.getListRootDir(), filePath);
        List<String> list = new ArrayList<>();
        List<List<String>> lines = null;

        try {
            lines = getFileContent(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // check that that lines is not null and that the list contains something
        if(lines != null && lines.size() > 0)
        {
            // Store header separate
            header = lines.get(0);
            lines.remove(0);
            items = lines;
        }
    }

    public int getNumberOfItems() {
        return items.size();
    }

    public String getLeftHeader() {
        return header.get(LEFT_IDX);
    }

    public String getRightHeader() {
        return header.get(RIGHT_IDX);
    }
}
