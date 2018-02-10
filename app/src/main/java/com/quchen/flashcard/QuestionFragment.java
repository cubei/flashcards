package com.quchen.flashcard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class QuestionFragment extends Fragment {

    private List<QuestionItem> questionItems = new ArrayList<>();
    private List<QuestionResult> questionResults = new ArrayList<>();

    private int numberOfDesiredQuestions;
    private int questionCount = 0;
    private int wrongAnswerCount = 0;
    private int correctAnswerCount = 0;

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
        answerTextViews.add((TextView) getView().findViewById(R.id.tv_answer1));
        answerTextViews.add((TextView) getView().findViewById(R.id.tv_answer2));
        answerTextViews.add((TextView) getView().findViewById(R.id.tv_answer3));
        answerTextViews.add((TextView) getView().findViewById(R.id.tv_answer4));

        timeTextView = getView().findViewById(R.id.tv_time);
        progressTextView = getView().findViewById(R.id.tv_progress);
        listNameTextView = getView().findViewById(R.id.tv_listName);
        questionTextView = getView().findViewById(R.id.tv_question);
        questionSideLabel = getView().findViewById(R.id.tv_questionSide);
        answerSideLabel = getView().findViewById(R.id.tv_guessSide);
        correctCountTextView = getView().findViewById(R.id.tv_correctAnswerCount);
        wrongCountTextView = getView().findViewById(R.id.tv_wrongAnswerCount);
    }

    private void setUpViews() {
        setQuestionItem(questionItems.get(0));
        for(TextView tv: answerTextViews) {
            tv.setOnClickListener(answerOnClick);
        }
        updateViews();
    }

    private void updateViews() {
        progressTextView.setText(String.format("%d / %d", questionCount, numberOfDesiredQuestions));
        correctCountTextView.setText(String.format("%d", correctAnswerCount));
        wrongCountTextView.setText(String.format("%d", wrongAnswerCount));
    }

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
        }
    }

    private void setUpNextQuestion() {
        questionCount++;

        if(questionCount == numberOfDesiredQuestions) {
            this.getActivity().finish();
        } else {
            setQuestionItem(questionItems.get(questionCount));
            updateViews();
        }
    }

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(int numberOfDesiredQuestions, List<QuestionItem> questionItems) {
        QuestionFragment fragment = new QuestionFragment();
        fragment.numberOfDesiredQuestions = numberOfDesiredQuestions;
        fragment.questionItems = questionItems;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assignViews();
        setUpViews();
    }
}
