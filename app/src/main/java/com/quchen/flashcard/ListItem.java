package com.quchen.flashcard;

/**
 * Created by Lars on 06.02.2018.
 */

public class ListItem {
    private String fileName;

    public ListItem(String fileName) {
        this.fileName = fileName;
    }

    public String getlabel() {
        return fileName;
    }
}
