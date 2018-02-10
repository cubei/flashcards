package com.quchen.flashcard;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
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

    private FrameLayout frameLayout;

    private int side;
    private int numberOfDesireditems;
    private boolean isTimeTrial;
    private boolean isTimeTrialReset;
    private int timePerItem;

    private QuestionFragment questionFragment;
    private ResultFragment resultFragment;

    public void questionsCompleted(List<QuestionResult> questionResults) {
        resultFragment = ResultFragment.newInstance(questionResults);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, resultFragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        side = getIntent().getIntExtra(KEY_SIDE, VAL_SIDE_LEFT);
        numberOfDesireditems = getIntent().getIntExtra(KEY_NUMBER_OF_ITEMS, 0);
        isTimeTrial = getIntent().getBooleanExtra(KEY_TIME_TRIAL, false);
        isTimeTrialReset = getIntent().getBooleanExtra(KEY_TIME_TRIAL_RESET, false);
        timePerItem = getIntent().getIntExtra(KEY_TIME_PER_ITEMS, 10);

        String[] listFiles = getIntent().getStringArrayExtra(KEY_FILE_LIST);

        List<QuestionItem> questionItems = new ArrayList<>();
        for(String listFile: listFiles) {
            ListItem listItem = new ListItem(listFile);
            questionItems.addAll(QuestionItem.getQuestionItemList(listItem));
        }

        numberOfDesireditems = Math.min(numberOfDesireditems, questionItems.size());

        questionFragment = QuestionFragment.newInstance(numberOfDesireditems, questionItems);
        frameLayout = findViewById(R.id.frameLayout);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, questionFragment);
        transaction.commit();
    }
}
