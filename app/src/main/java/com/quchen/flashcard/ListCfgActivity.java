package com.quchen.flashcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class ListCfgActivity extends AppCompatActivity {

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

    private void assignViews() {
        sideRadioGroup = findViewById(R.id.sideRadioGroup);
        leftRadioBtn = findViewById(R.id.radioButtonLeft);
        rightRadioBtn = findViewById(R.id.radioButtonRight);

        itemNumberSeekBar = findViewById(R.id.numberOfListItemsSlider);
        itemNumberTextView = findViewById(R.id.numberOfListItemsSliderLabel);

        timeTrialActiveCheckBox = findViewById(R.id.timeTrialActive);
        timeTrialSeekBarLayout = findViewById(R.id.layoutTimeSlider);
        timeTrialResetCheckBox = findViewById(R.id.timeTrialReset);
        timeTrialTimeSeekBar = findViewById(R.id.timeSlider);
        timeTrialTimeTextView = findViewById(R.id.timeSliderLabel);
    }

    private void setUpViews() {
        timeTrialActiveCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    timeTrialResetCheckBox.setVisibility(View.VISIBLE);
                    timeTrialSeekBarLayout.setVisibility(View.VISIBLE);
                } else {
                    timeTrialResetCheckBox.setVisibility(View.INVISIBLE);
                    timeTrialSeekBarLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        itemNumberSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                itemNumberTextView.setText("" + progress);
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
                timeTrialTimeTextView.setText("" + progress + " sec");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        leftRadioBtn.setText(multiListItem.getLeftHeader());
        rightRadioBtn.setText(multiListItem.getRightHeader());
    }

    private void restoreViewCfg() {
        // Defaults
        timeTrialResetCheckBox.setVisibility(View.INVISIBLE);
        timeTrialSeekBarLayout.setVisibility(View.INVISIBLE);

        sideRadioGroup.check(R.id.radioButtonLeft);

        // todo Restore previously saved config from last run
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cfg);

        String files[] = this.getIntent().getStringArrayExtra(ListActivity.KEY_FILE_LIST);
        multiListItem = new MultiListItem(files);

        assignViews();
        setUpViews();
        restoreViewCfg();
    }
}
