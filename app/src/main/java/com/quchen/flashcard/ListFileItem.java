package com.quchen.flashcard;

/**
 * Created by Lars on 06.02.2018.
 */

public class ListFileItem {
    private final String folderName;
    private final String fileName;

    public ListFileItem(String folderName, String fileName) {
        this.folderName = folderName;
        this.fileName = fileName;
    }

    public String getLabel() {
        int startOfExtention = fileName.toLowerCase().lastIndexOf(".csv");
        return fileName.substring(0, startOfExtention == -1 ? fileName.length() : startOfExtention);
    }

    public String getFilePath() {
        return folderName + "/" + fileName;
    }
}
