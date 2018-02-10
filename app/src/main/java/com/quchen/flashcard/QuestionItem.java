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

            questionItemList.add(new QuestionItem(listFilePath, question, rightAnswer, potentialWrongAnswers));
        }

        return questionItemList;
    }

    private String listFilePath;
    private String question;
    private String rightAnswer;
    private List<String> wrongAnswers;

    public QuestionItem(String listFilePath, String question, String rightAnswer, List<String> wrongAnswers) {
        this.listFilePath = listFilePath;
        this.question = question;
        this.rightAnswer = rightAnswer;
        this.wrongAnswers = wrongAnswers;
    }
}
