package com.quchen.flashcard;

/**
 * Created by Lars on 10.02.2018.
 */

public class QuestionResult {
    public String question;
    public String correctAnswer;
    public String givenAnswer;

    public QuestionResult(String question, String correctAnswer, String givenAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.givenAnswer = givenAnswer;
    }

    public boolean isAnswerCorrect() {
        return correctAnswer.equals(givenAnswer);
    }
}
