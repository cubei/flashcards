package com.quchen.flashcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    private List<QuestionResult> questionResults = new ArrayList<>();
    private int side;
    private int numberOfDesireditems;
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

    private int questionCount = 0;
    private int wrongAnswerCount = 0;
    private int correctAnswerCount = 0;

    private View.OnClickListener answerOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView answerTextView = (TextView) view;
            QuestionItem questionItem = questionItems.get(questionCount);
            QuestionResult questionResult = new QuestionResult(questionItem.question, questionItem.rightAnswer, answerTextView.getText().toString());
            questionResults.add(questionResult);

            if(questionResult.isAnswerCorrect()) {
                correctAnswerCount++;
            } else {
                wrongAnswerCount++;
            }

            setUpNextQuestion();
        }
    };

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

        updateViews();
    }

    private void updateViews() {
        progressTextView.setText(String.format("%d / %d", questionCount, numberOfDesireditems));
        correctCountTextView.setText(String.format("%d", correctAnswerCount));
        wrongCountTextView.setText(String.format("%d", wrongAnswerCount));
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
            TextView tv = answerTextViews.get(i);
            tv.setText(answers.get(i));
            tv.setOnClickListener(answerOnClick);
        }
    }

    private void setUpNextQuestion() {
        questionCount++;

        if(questionCount == numberOfDesireditems) {
            finish();
        } else {
            setQuestionItem(questionItems.get(questionCount));
            updateViews();
        }
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

        for(String listFile: listFiles) {
            ListItem listItem = new ListItem(listFile);
            questionItems.addAll(QuestionItem.getQuestionItemList(listItem));
        }

        numberOfDesireditems = Math.min(numberOfDesireditems, questionItems.size());

        assignViews();
        setUpViews();

        setQuestionItem(questionItems.get(questionCount));
    }
}
