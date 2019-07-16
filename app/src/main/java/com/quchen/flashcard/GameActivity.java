package com.quchen.flashcard;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    public static final String KEY_FILE_LIST = "file";

    public static final int NUMBER_OF_ANSWERS = 4;

    public static final int VAL_SIDE_LEFT = 0;
    public static final int VAL_SIDE_RIGHT = 1;

    private String[] listFiles;

    private long startTimestampMs;

    private ListCfgFragment.CfgContainer cfgContainer;
    private List<QuestionItem> questionItems;

    public void questionsCompleted(List<QuestionResult> questionResults) {
        long questionTimeMs = System.currentTimeMillis() - startTimestampMs;
        ResultFragment resultFragment = ResultFragment.newInstance(questionResults, questionTimeMs);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, resultFragment);
        transaction.commit();
    }

    private void startQuestionFragment() {
        int numberOfQuestions = Math.min(cfgContainer.numberOfDesireditems, questionItems.size());

        QuestionFragment questionFragment = QuestionFragment.newInstance(numberOfQuestions, questionItems);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, questionFragment);
        transaction.commit();

        startTimestampMs = System.currentTimeMillis();
    }

    public void restartLastGame() {
        startQuestionFragment();
    }

    /**
     * Restarts the last game, but uses only questions from the given questionResults
     * @param questionResultFilter
     */
    public void restartLastGame(List<QuestionResult> questionResultFilter) {
        this.cfgContainer.numberOfDesireditems = questionResultFilter.size();

        List<QuestionItem> filteredQuestionItems = new ArrayList<>();
        for(QuestionResult questionResult: questionResultFilter) {
            for(QuestionItem questionItem: this.questionItems) {
                if(   questionResult.question.equals(questionItem.question)
                   && questionResult.correctAnswer.equals(questionItem.rightAnswer)) {
                    filteredQuestionItems.add(questionItem);
                }
            }
        }

        this.questionItems = filteredQuestionItems;

        startQuestionFragment();
    }

    public void startGame(ListCfgFragment.CfgContainer cfgContainer) {
        this.cfgContainer = cfgContainer;

        List<QuestionItem> questionItems = new ArrayList<>();
        for(String listFile: listFiles) {
            ListItem listItem = new ListItem(listFile);
            questionItems.addAll(QuestionItem.getQuestionItemList(listItem, cfgContainer.side));
        }
        Collections.shuffle(questionItems);
        this.questionItems = questionItems;

        startQuestionFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        listFiles = getIntent().getStringArrayExtra(KEY_FILE_LIST);

        ListCfgFragment listCfgFragment = ListCfgFragment.newInstance(listFiles);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, listCfgFragment);
        transaction.commit();
    }
}
