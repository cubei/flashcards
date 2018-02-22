package com.quchen.flashcard;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lars on 10.02.2018.
 */

public class QuestionItem {

    private static String getQuestion(ListItem.ItemPair itemPair, int side) {
        return side == GameActivity.VAL_SIDE_LEFT ? itemPair.left : itemPair.right;
    }
    private static String getAnswer(ListItem.ItemPair itemPair, int side) {
        return side == GameActivity.VAL_SIDE_LEFT ? itemPair.right : itemPair.left;
    }

    private static String getQuestionHeader(ListItem listItem, int side) {
        return side == GameActivity.VAL_SIDE_LEFT ? listItem.getLeftHeader() : listItem.getRightHeader();
    }
    private static String getAnswerHeader(ListItem listItem, int side) {
        return side == GameActivity.VAL_SIDE_LEFT ? listItem.getRightHeader() : listItem.getLeftHeader();
    }

    public static List<QuestionItem> getQuestionItemList(ListItem listItem, int side) {
        List<QuestionItem> questionItemList = new ArrayList<>();

        for(ListItem.ItemPair itemPair: listItem.getItemPairs()) {
            String listFilePath = listItem.getFilePath();
            String question = getQuestion(itemPair, side);
            String rightAnswer = getAnswer(itemPair, side);

            List<String> potentialWrongAnswers = new ArrayList<>();
            for(ListItem.ItemPair ip: listItem.getItemPairs()) {
                if(!getQuestion(ip, side).equals(question) && !getAnswer(ip, side).equals(rightAnswer) && !potentialWrongAnswers.contains(getAnswer(ip, side))) {
                    potentialWrongAnswers.add(getAnswer(ip, side));
                }
            }

            questionItemList.add(new QuestionItem(getQuestionHeader(listItem, side), getAnswerHeader(listItem, side), listFilePath, question, rightAnswer, potentialWrongAnswers));
        }

        return questionItemList;
    }

    public String questionHeader;
    public String answerHeader;
    public String listFilePath;
    public String question;
    public String rightAnswer;
    public List<String> wrongAnswers;

    private QuestionItem(String questionHeader, String answerHeader, String listFilePath, String question, String rightAnswer, List<String> wrongAnswers) {
        this.questionHeader = questionHeader;
        this.answerHeader = answerHeader;
        this.listFilePath = listFilePath;
        this.question = question;
        this.rightAnswer = rightAnswer;
        this.wrongAnswers = wrongAnswers;

        // Fill wrong answer lists to make sure the list contains enough items to fill all flash cards
        int numberOfMissingAnswers = GameActivity.NUMBER_OF_ANSWERS - wrongAnswers.size() - 1; // minus the correct answer
        for(int i = 0; i < numberOfMissingAnswers; i++) {
            wrongAnswers.add("");
        }
    }
}
