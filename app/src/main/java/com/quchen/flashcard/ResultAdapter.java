package com.quchen.flashcard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Lars on 10.02.2018.
 */

public class ResultAdapter extends ArrayAdapter<QuestionResult> {

    public ResultAdapter(ResultFragment resultFragment) {
        super(resultFragment.getContext(), 0);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionResult item = this.getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.result_item_view, parent, false);
        }

        TextView questionLabel = convertView.findViewById(R.id.tv_questionLabel);
        questionLabel.setText(item.questionHeader);
        TextView questionVal = convertView.findViewById(R.id.tv_questionVal);
        questionVal.setText(item.question);
        TextView correctAnswerLabel = convertView.findViewById(R.id.tv_correctAnswerLabel);
        correctAnswerLabel.setText(item.answerHeader);
        TextView correctAnswerVal = convertView.findViewById(R.id.tv_correctAnswerVal);
        correctAnswerVal.setText(item.correctAnswer);

        if(item.isAnswerCorrect()) {
            LinearLayout selectedAnswerlayout = convertView.findViewById(R.id.selectedAnswerLayout);
            selectedAnswerlayout.setVisibility(View.GONE);
        } else {
            TextView selectedAnswerLabel = convertView.findViewById(R.id.tv_selectedAnswerLabel);
            TextView selectedAnswerVal = convertView.findViewById(R.id.tv_selectedAnswerVal);
            selectedAnswerVal.setText(item.givenAnswer);
        }

        return convertView;
    }
}
