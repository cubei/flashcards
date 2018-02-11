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

import java.util.List;

/**
 * Created by Lars on 10.02.2018.
 */

public class ResultFragment extends Fragment {
    private ResultAdapter resultAdapter;
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

        TextView resultScore = view.findViewById(R.id.tv_resultScore);
        resultScore.setText(String.format("%d / %d in %d sec (Ø %d sec)",
                            getNumberOfCorrectAnswers(), questionResults.size(), questionTimeS, questionTimeS/questionResults.size()));

        resultAdapter = new ResultAdapter(this);
        resultAdapter.addAll(questionResults);

        ListView listView = view.findViewById(R.id.resultList);
        listView.setAdapter(resultAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button retryAll = view.findViewById(R.id.btn_playAgainAll);
        Button retryWrong = view.findViewById(R.id.btn_playAgainWrong);
    }
}
