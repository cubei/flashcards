package com.quchen.flashcard;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GameActivity extends AppCompatActivity {

    public static final String KEY_FILE_LIST = "file";

    public static final int NUMBER_OF_ANSWERS = 4;

    public static final int VAL_SIDE_LEFT = 0;
    public static final int VAL_SIDE_RIGHT = 1;

    private String[] listFiles;

    private long startTimestampMs;

    private ListCfgFragment.CfgContainer cfgContainer;
    private List<QuestionItem> questionItems;

    public void questionsCompleted(List<QuestionResult> questionResults) {
        long questionTimeMs = System.currentTimeMillis() - startTimestampMs;
        ResultFragment resultFragment = ResultFragment.newInstance(questionResults, questionTimeMs);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, resultFragment);
        transaction.commit();
    }

    private void startQuestionFragment() {
        int numberOfQuestions = Math.min(cfgContainer.numberOfDesireditems, questionItems.size());

        QuestionFragment questionFragment = QuestionFragment.newInstance(
                numberOfQuestions,
                questionItems,
                cfgContainer.pauseOnError,
                cfgContainer.useTextToSpeech && cfgContainer.speakQuestion,
                cfgContainer.useTextToSpeech && cfgContainer.speakAnswer);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, questionFragment);
        transaction.commit();

        startTimestampMs = System.currentTimeMillis();
    }

    public void restartLastGame() {
        startQuestionFragment();
    }

    /**
     * Restarts the last game, but uses only questions from the given questionResults
     */
    public void restartLastGame(List<QuestionResult> questionResultFilter) {
        this.cfgContainer.numberOfDesireditems = questionResultFilter.size();

        List<QuestionItem> filteredQuestionItems = new ArrayList<>();
        for(QuestionResult questionResult: questionResultFilter) {
            for(QuestionItem questionItem: this.questionItems) {
                if(   questionResult.question.equals(questionItem.question)
                   && questionResult.correctAnswer.equals(questionItem.rightAnswer)) {
                    filteredQuestionItems.add(questionItem);
                }
            }
        }

        this.questionItems = filteredQuestionItems;

        startQuestionFragment();
    }

    public void startGame(ListCfgFragment.CfgContainer cfgContainer) {
        this.cfgContainer = cfgContainer;

        List<QuestionItem> questionItems = new ArrayList<>();
        for(String listFile: listFiles) {
            ListItem listItem = new ListItem(listFile);
            questionItems.addAll(QuestionItem.getQuestionItemList(listItem, cfgContainer.side));
        }
        Collections.shuffle(questionItems);
        this.questionItems = questionItems;

        startQuestionFragment();
    }


    private TextToSpeech textToSpeechEngine;

    private void setUpTTSE() {
        textToSpeechEngine = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeechEngine.setLanguage(Locale.US);
            }
        });
    }

    public void speakText(String text, String header) {
        textToSpeechEngine.setLanguage(getLocaleFromHeader(header));
        speakText(text);
    }
    public void speakText(String text) {
        textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1");
    }

    // https://stackoverflow.com/questions/26357938/detect-chinese-character-in-java/26358371
    private static boolean containsHanScript(String s) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return s.codePoints().anyMatch(codepoint -> Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
        } else {
            return false;
        }
    }

    private Locale getLocaleFromHeader(String header) {
        header = header.toLowerCase();
        Locale locale;

        if (header.contains("engli")) {
            locale = Locale.UK;
        } else if (header.contains("jap")
                || header.contains("日本")
                || header.contains("rōmaji")
                || header.contains("romaji")
                || header.contains("kanji")
                || header.contains("hiragana")
                || header.contains("katakana")) {
            locale = Locale.JAPAN;
        } else if (header.contains("korea")) {
            locale = Locale.KOREA;
        } else if (header.contains("chin")
                || header.contains("中文")
                || header.contains("pinyin")
                || containsHanScript(header)) {
            locale = Locale.CHINA;
        } else if (header.contains("deu")
                || header.contains("german")) {
            locale = Locale.GERMANY;
        } else if (header.contains("french")
                || header.contains("fran")) {
            locale = Locale.FRANCE;
        } else if (header.contains("ital")) {
            locale = Locale.ITALY;
        } else {
            locale = Locale.US;
        }

        return locale;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        listFiles = getIntent().getStringArrayExtra(KEY_FILE_LIST);

        ListCfgFragment listCfgFragment = ListCfgFragment.newInstance(listFiles);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, listCfgFragment);
        transaction.commit();
        setUpTTSE();
    }
}
