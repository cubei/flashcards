package com.quchen.flashcard;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;


public class ListCfgFragment extends Fragment {

    private MultiListItem multiListItem;

    private RadioGroup sideRadioGroup;
    private RadioButton leftRadioBtn;
    private RadioButton rightRadioBtn;

    private SeekBar itemNumberSeekBar;
    private TextView itemNumberTextView;

    private CheckBox timeTrialActiveCheckBox;
    private CheckBox timeTrialResetCheckBox;
    private LinearLayout timeTrialSeekBarLayout;
    private SeekBar timeTrialTimeSeekBar;
    private TextView timeTrialTimeTextView;

    private Button startButton;

    public class CfgContainer {
        public int side;
        public int numberOfDesireditems;
        public boolean isTimeTrial;
        public boolean isTimeTrialReset;
        public int timePerItem;
    }
    private CfgContainer cfgContainer = new CfgContainer();

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

        timeTrialActiveCheckBox = view.findViewById(R.id.timeTrialActive);
        timeTrialSeekBarLayout = view.findViewById(R.id.layoutTimeSlider);
        timeTrialResetCheckBox = view.findViewById(R.id.timeTrialReset);
        timeTrialTimeSeekBar = view.findViewById(R.id.timeSlider);
        timeTrialTimeTextView = view.findViewById(R.id.timeSliderLabel);

        startButton = view.findViewById(R.id.startBtn);
    }

    private void setUpViews() {
        timeTrialActiveCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    timeTrialResetCheckBox.setVisibility(View.VISIBLE);
                    timeTrialSeekBarLayout.setVisibility(View.VISIBLE);
                    cfgContainer.isTimeTrial = true;
                } else {
                    timeTrialResetCheckBox.setVisibility(View.INVISIBLE);
                    timeTrialSeekBarLayout.setVisibility(View.INVISIBLE);
                    cfgContainer.isTimeTrial = false;
                }
            }
        });

        timeTrialResetCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    cfgContainer.isTimeTrialReset = true;
                } else {
                    cfgContainer.isTimeTrialReset = false;
                }
            }
        });

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

        timeTrialTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeTrialTimeTextView.setText(String.format("%d sec", progress));
                cfgContainer.timePerItem = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        timeTrialTimeSeekBar.setProgress(10);

        leftRadioBtn.setText(multiListItem.getLeftHeader());
        rightRadioBtn.setText(multiListItem.getRightHeader());

        sideRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radioButtonLeft:
                        cfgContainer.side = GameActivity.VAL_SIDE_LEFT;
                        break;
                    case R.id.radioButtonRight:
                        cfgContainer.side = GameActivity.VAL_SIDE_RIGHT;
                        break;
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GameActivity)getActivity()).startGame(cfgContainer);
            }
        });
    }

    private void restoreViewCfg() {
        // Defaults
        timeTrialResetCheckBox.setVisibility(View.INVISIBLE);
        timeTrialSeekBarLayout.setVisibility(View.INVISIBLE);

        sideRadioGroup.check(R.id.radioButtonLeft);

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
