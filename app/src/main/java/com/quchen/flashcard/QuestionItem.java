package com.quchen.flashcard;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lars on 10.02.2018.
 */

public class QuestionItem {

    public static List<QuestionItem> getQuestionItemList(ListItem listItem) {
        List<QuestionItem> questionItemList = new ArrayList<>();

        for(ListItem.ItemPair itemPair: listItem.getItemPairs()) {
            String listFilePath = listItem.getFilePath();
            String question = itemPair.left;
            String rightAnswer = itemPair.right;

            List<String> potentialWrongAnswers = new ArrayList<>();
            for(ListItem.ItemPair ip: listItem.getItemPairs()) {
                if(!ip.left.equals(question) && !ip.right.equals(rightAnswer) && !potentialWrongAnswers.contains(ip.left)) {
                    potentialWrongAnswers.add(ip.left);
                }
            }

            questionItemList.add(new QuestionItem(listItem.getLeftHeader(), listItem.getRightHeader(), listFilePath, question, rightAnswer, potentialWrongAnswers));
        }

        return questionItemList;
    }

    public String questionHeader;
    public String answerHeader;
    public String listFilePath;
    public String question;
    public String rightAnswer;
    public List<String> wrongAnswers;

    public QuestionItem(String questionHeader, String answerHeader, String listFilePath, String question, String rightAnswer, List<String> wrongAnswers) {
        this.questionHeader = questionHeader;
        this.answerHeader = answerHeader;
        this.listFilePath = listFilePath;
        this.question = question;
        this.rightAnswer = rightAnswer;
        this.wrongAnswers = wrongAnswers;
    }
}
