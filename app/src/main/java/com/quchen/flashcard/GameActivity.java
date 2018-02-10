package com.quchen.flashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    public static final String KEY_FILE_LIST = "file";
    public static final String KEY_SIDE = "side";
    public static final int VAL_SIDE_LEFT = 0;
    public static final int VAL_SIDE_RIGHT = 1;
    public static final String KEY_NUMBER_OF_ITEMS = "numberOfItems";
    public static final String KEY_TIME_TRIAL = "timeTrial";
    public static final String KEY_TIME_TRIAL_RESET = "timeTrialReset";
    public static final String KEY_TIME_PER_ITEMS = "timePerItem";

    private List<QuestionItem> questionItems = new ArrayList<>();
    private int side;
    private int numberOfitems;
    private boolean isTimeTrial;
    private boolean isTimeTrialReset;
    private int timePerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        side = getIntent().getIntExtra(KEY_SIDE, VAL_SIDE_LEFT);
        numberOfitems = getIntent().getIntExtra(KEY_NUMBER_OF_ITEMS, 0);
        isTimeTrial = getIntent().getBooleanExtra(KEY_TIME_TRIAL, false);
        isTimeTrialReset = getIntent().getBooleanExtra(KEY_TIME_TRIAL_RESET, false);
        timePerItem = getIntent().getIntExtra(KEY_TIME_PER_ITEMS, 10);

        String[] listFiles = getIntent().getStringArrayExtra(KEY_FILE_LIST);

        for(String listFile: listFiles) {
            ListItem listItem = new ListItem(listFile);
            questionItems.addAll(QuestionItem.getQuestionItemList(listItem));
        }
    }
}
