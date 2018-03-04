package com.quchen.flashcard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lars on 10.02.2018.
 */

public class ResultFragment extends Fragment {
    private List<QuestionResult> questionResults;
    private long questionTimeS;

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance(List<QuestionResult> questionResults, long questionTimeMs) {
        ResultFragment fragment = new ResultFragment();
        fragment.questionResults = questionResults;
        fragment.questionTimeS = questionTimeMs / 1000;
        return fragment;
    }

    private int getNumberOfCorrectAnswers() {
        int correctCount = 0;

        for(QuestionResult questionResult: questionResults) {
            if(questionResult.isAnswerCorrect()) {
                correctCount++;
            }
        }

        return correctCount;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        final int numberOfCorrectAnswers = getNumberOfCorrectAnswers();
        final boolean noMistakes = numberOfCorrectAnswers == questionResults.size();
        final float relativeNumberOfRightAnswers = (float) numberOfCorrectAnswers / questionResults.size();

        TextView resultScore = view.findViewById(R.id.tv_resultScore);
        String timeFormat = String.format(Locale.US, "%02d:%02d", questionTimeS/60, questionTimeS%60);
        resultScore.setText(String.format(Locale.US, "%d / %d (%d%%) in %s (Ø %.1f sec)",
                numberOfCorrectAnswers, questionResults.size(), (int) (relativeNumberOfRightAnswers*100), timeFormat, (float)questionTimeS/questionResults.size()));

        TextView resultComment = view.findViewById(R.id.tv_resultComment);
        if(noMistakes) {
            resultComment.setText(R.string.resultComment100);
        } else if(relativeNumberOfRightAnswers < 0.5f) {
            resultComment.setText(R.string.resultCommentLess50);
        } else {
            resultComment.setVisibility(View.GONE);
        }

        Button retryWrong = view.findViewById(R.id.btn_playAgainWrong);
        if(noMistakes) {
            retryWrong.setVisibility(View.GONE);
        }

        ResultAdapter resultAdapter = new ResultAdapter(this);
        resultAdapter.addAll(questionResults);

        ListView listView = view.findViewById(R.id.resultList);
        listView.setAdapter(resultAdapter);

        return view;
    }

    private View.OnClickListener retryAllOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((GameActivity)getActivity()).restartLastGame();
        }
    };

    private View.OnClickListener retryWrongOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            List<QuestionResult> wrongQuestionResults = new ArrayList<>();

            for(QuestionResult questionResult: questionResults) {
                if(!questionResult.isAnswerCorrect()) {
                    wrongQuestionResults.add(questionResult);
                }
            }

            ((GameActivity)getActivity()).restartLastGame(wrongQuestionResults);
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button retryAll = view.findViewById(R.id.btn_playAgainAll);
        retryAll.setOnClickListener(retryAllOnClick);
        Button retryWrong = view.findViewById(R.id.btn_playAgainWrong);
        retryWrong.setOnClickListener(retryWrongOnClick);
    }
}
