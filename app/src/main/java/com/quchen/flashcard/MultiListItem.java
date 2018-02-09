package com.quchen.flashcard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lars on 09.02.2018.
 */

public class MultiListItem {
    private List<ListItem> listItems = new ArrayList<>();

    public MultiListItem(String filesPaths[]) {
        for(String filePath: filesPaths) {
            listItems.add(new ListItem(filePath));
        }
    }

    public int getNumberOfItems() {
        int number = 0;

        for(ListItem listItem: listItems) {
            number += listItem.getNumberOfItems();
        }

        return number;
    }
}
