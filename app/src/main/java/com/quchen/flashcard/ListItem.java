package com.quchen.flashcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lars on 09.02.2018.
 */

public class ListItem {

    private static final int LEFT_IDX = 0;
    private static final int RIGHT_IDX = 1;

    private static final String SEPARATOR = ";";

    private final String filePath;

    public String getFilePath() {
        return filePath;
    }

    public static class ItemPair {
        public String left;
        public String right;

        public ItemPair(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }

    private ItemPair header;
    private final List<ItemPair> itemPairs = new ArrayList<>();

    public ListItem(String filePath) {
        this.filePath = filePath;
        readFile();
    }
    public ListItem(String filePath, String left, String right) {
        this.filePath = filePath;
        this.header = new ItemPair(left, right);
    }

    public List<ItemPair> getItemPairs() {
        return itemPairs;
    }

    private static List<List<String>> getFileContent(File file) throws IOException {

        List<List<String>> fileContentList = new ArrayList<>();

        if(file.exists()) {

            InputStream inputStream = new FileInputStream(file);
            Reader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);

            String line;

            while ((line = br.readLine()) != null) {
                if(line.contains(SEPARATOR)) {
                    ArrayList<String> values = new ArrayList<>(Arrays.asList(line.split(SEPARATOR)));
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
            List<String> headerLine = lines.get(0);
            header = new ItemPair(headerLine.get(LEFT_IDX), headerLine.get(RIGHT_IDX));
            lines.remove(0);
            for(List<String> line: lines) {
                if(line.size() >= 2) {
                    itemPairs.add(new ItemPair(line.get(LEFT_IDX).trim(), line.get(RIGHT_IDX).trim()));
                }
            }
        }
    }

    protected void saveToFile()
    {
        File file = new File(App.getListRootDir(), filePath);

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file, false));
            outputStreamWriter.write(String.format("%s%s%s\n", header.left, SEPARATOR, header.right));
            for(ItemPair itempair: itemPairs) {
                outputStreamWriter.write(String.format("%s%s%s\n", itempair.left, SEPARATOR, itempair.right));
            }
            outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfItems() {
        return itemPairs.size();
    }

    public String getLeftHeader() {
        if (header != null)
        {
            return header.left;
        }
        else
        {
            return null;
        }
    }

    public String getRightHeader() {
        if(header != null)
        {
            return header.right;
        }
        else
        {
            return null;
        }
    }
}
