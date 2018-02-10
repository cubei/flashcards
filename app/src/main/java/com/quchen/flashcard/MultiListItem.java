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

    private static String getCompatibleHeader(List<String> headers, String defaultName) {
        String header = headers.get(0);
        headers.remove(0);

        for(String h: headers) {
            if(!header.toLowerCase().equals(h.toLowerCase())) {
                header = defaultName;
                break;
            }
        }

        return header;
    }

    public String getLeftHeader() {
        List<String> headers = new ArrayList<>();
        for(ListItem li: listItems) {
            headers.add(li.getLeftHeader());
        }

        return getCompatibleHeader(headers, App.getContext().getString(R.string.listCfg_radioBtn_Left));
    }

    public String getRightHeader() {
        List<String> headers = new ArrayList<>();
        for(ListItem li: listItems) {
            headers.add(li.getRightHeader());
        }

        return getCompatibleHeader(headers, App.getContext().getString(R.string.listCfg_radioBtn_Right));
    }
}
