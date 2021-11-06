package com.quchen.flashcard;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


public class ListCfgFragment extends Fragment {

    private MultiListItem multiListItem;

    private RadioGroup sideRadioGroup;
    private RadioButton leftRadioBtn;
    private RadioButton rightRadioBtn;

    private SeekBar itemNumberSeekBar;
    private TextView itemNumberTextView;

    private CheckBox pauseOnErrorChkBx;

    private CheckBox useTextToSpeechChkBx;
    private RadioGroup speechRadioGroup;

    private Button startButton;

    public static class CfgContainer {
        public int side;
        public int numberOfDesireditems;
        public boolean pauseOnError;
        public boolean useTextToSpeech;
        public boolean speakQuestion;
        public boolean speakAnswer;
    }
    private final CfgContainer cfgContainer = new CfgContainer();

    public ListCfgFragment() {}

    public static ListCfgFragment newInstance(String[] listFiles) {
        ListCfgFragment fragment = new ListCfgFragment();
        fragment.multiListItem = new MultiListItem(listFiles);
        return fragment;
    }

    private void assignViews(View view) {
        sideRadioGroup = view.findViewById(R.id.sideRadioGroup);
        leftRadioBtn = view.findViewById(R.id.radioButtonLeft);
        rightRadioBtn = view.findViewById(R.id.radioButtonRight);

        itemNumberSeekBar = view.findViewById(R.id.numberOfListItemsSlider);
        itemNumberTextView = view.findViewById(R.id.numberOfListItemsSliderLabel);

        pauseOnErrorChkBx = view.findViewById(R.id.pauseOnErrorChkBx);

        useTextToSpeechChkBx = view.findViewById(R.id.useTextToSpeechChkBx);
        speechRadioGroup = view.findViewById(R.id.textToSpeechSideRadioGroup);

        startButton = view.findViewById(R.id.startBtn);
    }

    private void setUpViews() {


        itemNumberSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                itemNumberTextView.setText("" + progress);
                cfgContainer.numberOfDesireditems = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        int numberOfItems = multiListItem.getNumberOfItems();
        itemNumberSeekBar.setMax(numberOfItems);
        itemNumberSeekBar.setProgress(numberOfItems);

        leftRadioBtn.setText(multiListItem.getLeftHeader());
        rightRadioBtn.setText(multiListItem.getRightHeader());

        sideRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch(i) {
                case R.id.radioButtonLeft:
                    cfgContainer.side = GameActivity.VAL_SIDE_LEFT;
                    break;
                case R.id.radioButtonRight:
                    cfgContainer.side = GameActivity.VAL_SIDE_RIGHT;
                    break;
            }
        });

        pauseOnErrorChkBx.setOnCheckedChangeListener((compoundButton, isChecked) -> cfgContainer.pauseOnError = isChecked);

        useTextToSpeechChkBx.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            cfgContainer.useTextToSpeech = isChecked;
            if(isChecked) {
                speechRadioGroup.setVisibility(View.VISIBLE);
            } else {
                speechRadioGroup.setVisibility(View.GONE);
            }
        });

        speechRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch(i) {
                case R.id.radioButtonSpeakQuestion:
                    cfgContainer.speakQuestion = true;
                    cfgContainer.speakAnswer = false;
                    break;
                case R.id.radioButtonSpeakAnswer:
                    cfgContainer.speakQuestion = false;
                    cfgContainer.speakAnswer = true;
                    break;
            }
        });

        startButton.setOnClickListener(view -> ((GameActivity)getActivity()).startGame(cfgContainer));
    }

    private void restoreViewCfg() {
        // Defaults
        sideRadioGroup.check(R.id.radioButtonLeft);
        pauseOnErrorChkBx.setChecked(false);
        useTextToSpeechChkBx.setChecked(false);
        speechRadioGroup.setVisibility(View.GONE);
        speechRadioGroup.check(R.id.radioButtonSpeakQuestion);

        // todo Restore previously saved config from last run
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_cfg, container, false);

        assignViews(view);
        setUpViews();
        restoreViewCfg();

        return view;
    }
}
