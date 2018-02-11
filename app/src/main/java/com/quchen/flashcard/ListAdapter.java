package com.quchen.flashcard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lars on 05.02.2018.
 */

public class ListAdapter extends ArrayAdapter<ListFileItem> {

    private final ListActivity listActivity;
    private final ListView listView;
    private Button startBtn;
    private List<ListFileItem> listOfSelectedItems = new ArrayList<>();

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            ListFileItem listItem = getItem(position);
            listActivity.startListFile(listItem.getFilePath());
        }
    };

    private View.OnClickListener watchBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final View parentRow = (View) view.getParent().getParent();
            final int position = listView.getPositionForView(parentRow);
            final ListFileItem listFileItem = getItem(position);
            listActivity.startListView(listFileItem);
        }
    };

    private CompoundButton.OnCheckedChangeListener selectionChanged = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            final View parentRow = (View) compoundButton.getParent().getParent();
            final int position = listView.getPositionForView(parentRow);
            ListFileItem listItem = getItem(position);

            if(checked) {
                listOfSelectedItems.add(listItem);
            } else {
                listOfSelectedItems.remove(listItem);
            }

            listActivity.changeStartBtnText(listOfSelectedItems.size() > 0);
        }
    };

    public List<ListFileItem> getListOfSelectedItems() {
        return listOfSelectedItems;
    }


    public ListAdapter(ListActivity listActivity, ListView listView) {
        super(listActivity, 0);

        this.listActivity = listActivity;
        this.listView = listView;

        listView.setOnItemClickListener(itemClickListener);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListFileItem item = this.getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_view, parent, false);
        }

        TextView label = convertView.findViewById(R.id.label);
        label.setText(item.getlabel());

        ImageButton watchBtn = convertView.findViewById(R.id.showListBtn);
        watchBtn.setOnClickListener(watchBtnClickListener);

        CheckBox checkBox = convertView.findViewById(R.id.selectList);
        checkBox.setOnCheckedChangeListener(selectionChanged);

        return convertView;
    }
}