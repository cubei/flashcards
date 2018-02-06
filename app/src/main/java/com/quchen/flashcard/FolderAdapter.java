package com.quchen.flashcard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lars on 05.02.2018.
 */

public class FolderAdapter extends ArrayAdapter<String> {

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