package com.quchen.flashcard;

/**
 * Created by Lars on 06.02.2018.
 */

public class ListFileItem {
    private String folderName;
    private String fileName;

    public ListFileItem(String folderName, String fileName) {
        this.folderName = folderName;
        this.fileName = fileName;
    }

    public String getlabel() {
        return fileName;
    }

    public String getFilePath() {
        return folderName + "/" + fileName;
    }
}
