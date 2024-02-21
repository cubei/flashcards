package com.quchen.flashcard;

import android.app.AlertDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ListViewActivity extends AppCompatActivity {

    public static final String KEY_FILE = "file";

    private ListItem listItem;

    private static class ListViewAdapter extends ArrayAdapter<ListItem.ItemPair> {

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

        final String file = this.getIntent().getStringExtra(KEY_FILE);
        listItem = new ListItem(file);

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

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            ListItem.ItemPair itemPair = listViewAdapter.getItem(position);
            showItemEditDialog(itemPair);
            return true;
        });
    }

    public void addItemOnClick(View view) {
        showItemEditDialog(null);
    }

    private void showItemEditDialog(ListItem.ItemPair itemPair) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        View view = this.getLayoutInflater().inflate(R.layout.edit_item_view, null);
        alert.setView(view);
        final TextView leftLabel = view.findViewById(R.id.leftLabel);
        final TextView rightLabel = view.findViewById(R.id.rightLabel);
        final EditText leftEdit = view.findViewById(R.id.leftEdit);
        final EditText rightEdit = view.findViewById(R.id.rightEdit);

        String leftPrefill = "";
        String rightPrefill = "";
        if(itemPair != null) {
            leftPrefill = itemPair.left;
            rightPrefill = itemPair.right;
        }
        leftEdit.setText(leftPrefill);
        rightEdit.setText(rightPrefill);
        leftLabel.setText(listItem.getLeftHeader());
        rightLabel.setText(listItem.getRightHeader());

        alert.setPositiveButton(R.string.btn_addList, (dialog, id) -> {
            final String leftVal = leftEdit.getText().toString();
            final String rightVal = rightEdit.getText().toString();
            if(leftVal.isEmpty() || rightVal.isEmpty()){
                return;
            }

            if(itemPair != null) {
                Toast.makeText(ListViewActivity.this, String.format("\"%s : %s\" -> \"%s : %s\" %s", itemPair.left, itemPair.right, leftVal, rightVal, getString(R.string.changed)), Toast.LENGTH_SHORT).show();
                itemPair.left = leftVal;
                itemPair.right = rightVal;
            } else {
                this.listItem.getItemPairs().add(new ListItem.ItemPair(leftVal, rightVal));
                Toast.makeText(ListViewActivity.this, String.format("\"%s : %s\" %s", leftVal, rightVal, getString(R.string.added)), Toast.LENGTH_SHORT).show();
            }
            this.listItem.saveToFile();

            // Restart activity to reflect change
            if(itemPair == null) {
                finish();
                startActivity(getIntent());
            }
        });

        alert.setNegativeButton(R.string.delete, (dialog, id) -> {
            if(itemPair != null) {
                this.listItem.getItemPairs().remove(itemPair);
                this.listItem.saveToFile();
                Toast.makeText(ListViewActivity.this, String.format("\"%s : %s\" %s", itemPair.left, itemPair.right, getString(R.string.deleted)), Toast.LENGTH_SHORT).show();

                // Restart activity to reflect change
                finish();
                startActivity(getIntent());
            }
        });

        alert.show();
    }
}
