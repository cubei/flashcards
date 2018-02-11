package com.quchen.flashcard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewActivity extends AppCompatActivity {

    public static final String KEY_FILE = "file";

    private class ListViewAdapter extends ArrayAdapter<ListItem.ItemPair> {

        public ListViewAdapter(Context context) {
            super(context, 0);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItem.ItemPair item = this.getItem(position);

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.list_view_item_view, parent, false);
            }

            TextView leftTv = convertView.findViewById(R.id.leftVal);
            leftTv.setText(item.left);
            TextView rightTv = convertView.findViewById(R.id.rightVal);
            rightTv.setText(item.right);

            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        String file = this.getIntent().getStringExtra(KEY_FILE);
        ListItem listItem = new ListItem(file);

        TextView title = findViewById(R.id.listTitle);
        title.setText(listItem.getFilePath());

        TextView leftHeader = findViewById(R.id.leftHeader);
        leftHeader.setText(listItem.getLeftHeader());
        TextView rightHeader = findViewById(R.id.rightHeader);
        rightHeader.setText(listItem.getRightHeader());

        ListViewAdapter listViewAdapter = new ListViewAdapter(this);
        listViewAdapter.addAll(listItem.getItemPairs());

        ListView listView = findViewById(R.id.listList);
        listView.setAdapter(listViewAdapter);
    }
}
