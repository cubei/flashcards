package com.quchen.flashcard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class QuestionFragment extends Fragment {

    private boolean pauseOnError = true;

    private List<QuestionItem> questionItems = new ArrayList<>();
    private List<QuestionResult> questionResults = new ArrayList<>();

    private int numberOfDesiredQuestions;
    private int questionCount = 0;
    private int wrongAnswerCount = 0;
    private int correctAnswerCount = 0;

    private TextView timeTextView;
    private TextView progressTextView;
    private LinearLayout cardLayout;
    private LinearLayout nextCardLayout;
    private View answerLineLayout;
    private View correctAnswerLine;
    private View wrongAnswerLine;

    private void assignViews() {
        timeTextView = getView().findViewById(R.id.tv_time);
        progressTextView = getView().findViewById(R.id.tv_progress);
        cardLayout = getView().findViewById(R.id.cardLayout);
        nextCardLayout = getView().findViewById(R.id.nextCardLayout);
        answerLineLayout = getView().findViewById(R.id.answerLineLayout);
        correctAnswerLine = getView().findViewById(R.id.correctCntLine);
        wrongAnswerLine = getView().findViewById(R.id.wrongCntLine);
    }

    private void setUpViews() {
        setUpQuestionCards();

        for(TextView tv: getAnswerTextViews()) {
            tv.setOnClickListener(answerOnClick);
        }
        updateViews();
    }

    private void updateViews() {
        progressTextView.setText(String.format(Locale.US, "%s %d / %d", getResources().getString(R.string.question), questionCount + 1, numberOfDesiredQuestions));
    }

    ValueAnimator animateAnswerColor(View view, int color) {
        final GradientDrawable dw = (GradientDrawable) view.getBackground();
        int originalColor;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            originalColor = dw.getColor().getDefaultColor();
        } else {
            originalColor = getResources().getColor(R.color.colorPrimary);
        }
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), originalColor, color);
        colorAnimation.setDuration(300);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                dw.setColor((int)valueAnimator.getAnimatedValue());
            }
        });
        return colorAnimation;
    }

    /**
     * @param wrongTv Can be null in case the correct answer was selected
     * @param correctTv Can't be null.
     */
    private void animateAnswer(final TextView wrongTv, final TextView correctTv) {
        final ColorStateList defaultColor;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            defaultColor = ((GradientDrawable) correctTv.getBackground()).getColor();
        } else {
            defaultColor = null;
        }

        ValueAnimator colorAnimationCorrect = animateAnswerColor(correctTv, getResources().getColor(R.color.colorCorrectAnswer));
        colorAnimationCorrect.addListener(new AnimatorListenerAdapter() {

            private void flipCardAway() {
                cardLayout.animate()
                        .translationY(-getView().getHeight())
                        .translationX(getView().getWidth() / 6)
                        .setDuration(400)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                // Restore to normal color
                                if(wrongTv != null) {
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                        ((GradientDrawable)wrongTv.getBackground()).setColor(defaultColor);
                                    } else {
                                        ((GradientDrawable)wrongTv.getBackground()).setColor(getResources().getColor(R.color.colorPrimaryContainer));
                                    }
                                }
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    ((GradientDrawable)correctTv.getBackground()).setColor(defaultColor);
                                } else {
                                    ((GradientDrawable)correctTv.getBackground()).setColor(getResources().getColor(R.color.colorPrimaryContainer));
                                }
                                setUpNextQuestion();
                            }
                        });
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if(pauseOnError && (wrongTv != null)) {
                    cardLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cardLayout.setOnClickListener(null);
                            flipCardAway();
                        }
                    });
                } else {
                    flipCardAway();
                }
            }
        });

        if(wrongTv != null) {
            animateAnswerColor(wrongTv, getResources().getColor(R.color.colorWrongAnswer)).start();
        }
        colorAnimationCorrect.start();
    }

    private void drawAnswerLine() {
        answerLineLayout.setVisibility(View.VISIBLE);

        final int totalWidth = answerLineLayout.getWidth();
        // line is updated before questionCount is incremented
        final int correctWidth = totalWidth * correctAnswerCount / (questionCount+1);
        final int wrongWidth = totalWidth * wrongAnswerCount / (questionCount+1);

        correctAnswerLine.getLayoutParams().width = correctWidth;
        wrongAnswerLine.getLayoutParams().width = wrongWidth;

        correctAnswerLine.requestLayout();
        wrongAnswerLine.requestLayout();
    }

    private View.OnClickListener answerOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView answerTextView = (TextView) view;
            TextView correctTextView = null;
            String givenAnswer = answerTextView.getText().toString();
            QuestionItem questionItem = questionItems.get(questionCount);
            QuestionResult questionResult = new QuestionResult(questionItem.questionHeader, questionItem.answerHeader, questionItem.question, questionItem.rightAnswer, givenAnswer);
            questionResults.add(questionResult);

            if(questionResult.isAnswerCorrect()) {
                correctAnswerCount++;
            } else {
                wrongAnswerCount++;
            }

            drawAnswerLine();

            // Disable click event for all answers for the time of the answer animation
            for (TextView tv : getAnswerTextViews()) {
                tv.setClickable(false);
                if (tv.getText().toString().equals(questionItem.rightAnswer)) {
                    correctTextView = tv;
                }
            }

            animateAnswer(questionResult.isAnswerCorrect() ? null : answerTextView, correctTextView);
        }
    };

    private List<TextView> getAnswerTextViews() {
        return getAnswerTextViews(cardLayout);
    }

    private List<TextView> getAnswerTextViews(View layout) {
        List<TextView> answerTextViews = new ArrayList<>();
        View view;

        view = layout.findViewById(R.id.tv_answer1);
        answerTextViews.add((TextView) view.findViewById(R.id.answerTextView));
        view = layout.findViewById(R.id.tv_answer2);
        answerTextViews.add((TextView) view.findViewById(R.id.answerTextView));
        view = layout.findViewById(R.id.tv_answer3);
        answerTextViews.add((TextView) view.findViewById(R.id.answerTextView));
        view = layout.findViewById(R.id.tv_answer4);
        answerTextViews.add((TextView) view.findViewById(R.id.answerTextView));

        return answerTextViews;
    }

    // Store the shuffled list, so the nextCardView is identical to one placed above it
    private Map<QuestionItem, List<String>> questionAnswerMap = new HashMap<>();
    private List<String> getAnswerList(QuestionItem questionItem) {
        List<String> answers;
        if(questionAnswerMap.containsKey(questionItem)) {
            answers = questionAnswerMap.get(questionItem);
        } else {
            answers = new ArrayList<>();
            answers.add(questionItem.rightAnswer);

            List<String> wrongAnswers = new ArrayList<>(questionItem.wrongAnswers);
            for (int i = 0; i < (GameActivity.NUMBER_OF_ANSWERS - 1); i++) {
                String wrongAnswer = wrongAnswers.get((int) (Math.random() * wrongAnswers.size()));
                wrongAnswers.remove(wrongAnswer);
                answers.add(wrongAnswer);
            }

            Collections.shuffle(answers);

            questionAnswerMap.put(questionItem, answers);
        }

        return answers;
    }

    private void setQuestionItem(QuestionItem questionItem, View layout, boolean enableClicks) {
        ((TextView)layout.findViewById(R.id.tv_listName)).setText(questionItem.listName);
        ((TextView)layout.findViewById(R.id.tv_question)).setText(questionItem.question);
        ((TextView)layout.findViewById(R.id.tv_questionSide)).setText(questionItem.questionHeader);
        ((TextView)layout.findViewById(R.id.tv_guessSide)).setText(questionItem.answerHeader);


        List<TextView> answerTextViews = getAnswerTextViews(layout);

        List<String> answers = getAnswerList(questionItem);

        for(int i=0; i < GameActivity.NUMBER_OF_ANSWERS; i++) {
            TextView tv = answerTextViews.get(i);
            tv.setText(answers.get(i));
            if(enableClicks) {
                tv.setClickable(true);
            }
        }
    }

    private void setUpQuestionCards() {
        if(questionCount == numberOfDesiredQuestions) {
            ((GameActivity)this.getActivity()).questionsCompleted(questionResults);
        } else {
            setQuestionItem(questionItems.get(questionCount), cardLayout, true);
            updateViews();
        }

        // Reset view translation
        cardLayout.animate().translationY(0).translationX(0).setDuration(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if((questionCount + 1) < numberOfDesiredQuestions) {
                    setQuestionItem(questionItems.get(questionCount + 1), nextCardLayout, false);
                } else {
                    nextCardLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setUpNextQuestion() {
        questionCount++;
        setUpQuestionCards();
    }

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(int numberOfDesiredQuestions, List<QuestionItem> questionItems, boolean pauseOnError) {
        QuestionFragment fragment = new QuestionFragment();
        fragment.numberOfDesiredQuestions = numberOfDesiredQuestions;
        fragment.questionItems = questionItems;
        fragment.pauseOnError = pauseOnError;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionAnswerMap.clear();

        assignViews();

        answerLineLayout.setVisibility(View.INVISIBLE);
        timeTextView.setVisibility(View.INVISIBLE);

        setUpViews();
    }
}
