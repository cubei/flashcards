package com.quchen.flashcard;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    public static final String KEY_FILE_LIST = "file";

    public static final int VAL_SIDE_LEFT = 0;
    public static final int VAL_SIDE_RIGHT = 1;

    private FrameLayout frameLayout;

    private String[] listFiles;

    private long startTimestampMs;

    private ListCfgFragment listCfgFragment;
    private QuestionFragment questionFragment;
    private ResultFragment resultFragment;

    public void questionsCompleted(List<QuestionResult> questionResults) {
        long questionTimeMs = System.currentTimeMillis() - startTimestampMs;
        resultFragment = ResultFragment.newInstance(questionResults, questionTimeMs);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, resultFragment);
        transaction.commit();
    }

    public void startGame(ListCfgFragment.CfgContainer cfgContainer) {
        List<QuestionItem> questionItems = new ArrayList<>();
        for(String listFile: listFiles) {
            ListItem listItem = new ListItem(listFile);
            questionItems.addAll(QuestionItem.getQuestionItemList(listItem, cfgContainer.side));
        }

        cfgContainer.numberOfDesireditems = Math.min(cfgContainer.numberOfDesireditems, questionItems.size());

        questionFragment = QuestionFragment.newInstance(cfgContainer.numberOfDesireditems, questionItems);
        frameLayout = findViewById(R.id.frameLayout);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, questionFragment);
        transaction.commit();

        startTimestampMs = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        listFiles = getIntent().getStringArrayExtra(KEY_FILE_LIST);

        listCfgFragment = ListCfgFragment.newInstance(listFiles);

        frameLayout = findViewById(R.id.frameLayout);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, listCfgFragment);
        transaction.commit();
    }
}
