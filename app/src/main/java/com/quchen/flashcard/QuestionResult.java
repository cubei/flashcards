package com.quchen.flashcard;

/**
 * Created by Lars on 10.02.2018.
 */

public class QuestionResult {
    public final String questionHeader;
    public final String answerHeader;
    public final String question;
    public final String correctAnswer;
    public final String givenAnswer;

    public QuestionResult(String questionHeader, String answerHeader, String question, String correctAnswer, String givenAnswer) {
        this.questionHeader = questionHeader;
        this.answerHeader = answerHeader;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.givenAnswer = givenAnswer;
    }

    public boolean isAnswerCorrect() {
        return correctAnswer.equals(givenAnswer);
    }
}
