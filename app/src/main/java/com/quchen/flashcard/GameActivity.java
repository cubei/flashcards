package com.quchen.flashcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private List<QuestionItem> questionItems = new ArrayList<>();
    private int side;
    private int numberOfitems;
    private boolean isTimeTrial;
    private boolean isTimeTrialReset;
    private int timePerItem;

    private List<TextView> answerTextViews = new ArrayList<>();
    private TextView timeTextView;
    private TextView progressTextView;
    private TextView listNameTextView;
    private TextView questionTextView;
    private TextView questionSideLabel;
    private TextView answerSideLabel;
    private TextView correctCountTextView;
    private TextView wrongCountTextView;

    private void assignViews() {
        answerTextViews.add((TextView) findViewById(R.id.tv_answer1));
        answerTextViews.add((TextView) findViewById(R.id.tv_answer2));
        answerTextViews.add((TextView) findViewById(R.id.tv_answer3));
        answerTextViews.add((TextView) findViewById(R.id.tv_answer4));

        timeTextView = findViewById(R.id.tv_time);
        progressTextView = findViewById(R.id.tv_progress);
        listNameTextView = findViewById(R.id.tv_listName);
        questionTextView = findViewById(R.id.tv_question);
        questionSideLabel = findViewById(R.id.tv_questionSide);
        answerSideLabel = findViewById(R.id.tv_guessSide);
        correctCountTextView = findViewById(R.id.tv_correctAnswerCount);
        wrongCountTextView = findViewById(R.id.tv_wrongAnswerCount);
    }

    private void setUpViews() {
        correctCountTextView.setText(String.format("%d", 0));
        wrongCountTextView.setText(String.format("%d", 0));
    }

    private void setQuestionItem(QuestionItem questionItem) {
        listNameTextView.setText(questionItem.listFilePath);
        questionTextView.setText(questionItem.question);
        questionSideLabel.setText(questionItem.questionHeader);
        answerSideLabel.setText(questionItem.answerHeader);

        List<String> answers = new ArrayList<>();
        answers.add(questionItem.rightAnswer);

        List<String> wrongAnswers = new ArrayList<>(questionItem.wrongAnswers);
        for(int i=0; i<3; i++) {
            String wrongAnswer = wrongAnswers.get((int) (Math.random() * wrongAnswers.size()));
            wrongAnswers.remove(wrongAnswer);
            answers.add(wrongAnswer);
        }

        Collections.shuffle(answers);

        for(int i=0; i<4; i++) {
            answerTextViews.get(i).setText(answers.get(i));
        }
    }

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

        numberOfitems = Math.min(numberOfitems, questionItems.size());

        assignViews();
        setUpViews();

        setQuestionItem(questionItems.get(0));
    }
}
