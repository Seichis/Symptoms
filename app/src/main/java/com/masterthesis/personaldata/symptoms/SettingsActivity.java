package com.masterthesis.personaldata.symptoms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.masterthesis.personaldata.symptoms.DAO.model.Patient;
import com.masterthesis.personaldata.symptoms.DAO.model.Settings;
import com.masterthesis.personaldata.symptoms.managers.SettingsManager;

import java.sql.Timestamp;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    BackgroundService backgroundService;
    @Bind(R.id.activate_moves_switch)
    Switch movesSwitch;
    @Bind(R.id.activate_lifelog_switch)
    Switch lifelogSwitch;
    @Bind(R.id.activate_flic_switch)
    Switch flicSwitch;
    @Bind(R.id.radio_group_mode)
    RadioGroup radioGroup;
    Settings settings;

    @OnClick(R.id.apply_changes)
    void applyChanges() {
        SettingsManager.getInstance().updateSettings(settings);
        this.finish();
    }

    @OnClick(R.id.discard_changes)
    void discardChanges() {
        this.finish();
    }

    @OnCheckedChanged(R.id.activate_moves_switch)
    void activateMoves() {
        backgroundService.activateMoves(this);
        settings.setMovesActivated(true);
    }

    @OnCheckedChanged(R.id.activate_flic_switch)
    void activateFlicButton() {
        if (flicSwitch.isChecked()) {
            backgroundService.activateFlic();
            settings.setFlicActivated(true);
        } else {
            backgroundService.deactivateFlic();
            settings.setFlicActivated(false);
        }
    }

    @OnCheckedChanged(R.id.activate_lifelog_switch)
    void activateLifelog() {

    }

    @OnClick(R.id.radio_group_mode)
    void changeMode() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case (R.id.no_mode):
                settings.setMode(Settings.NO_BUTTON_MODE);
//                backgroundService.setMode(Settings.NO_BUTTON_MODE);
                break;
            case (R.id.binary_mode):
                settings.setMode(Settings.BINARY_BUTTON_MODE);

//                backgroundService.setMode(Settings.BINARY_BUTTON_MODE);
                break;
            case (R.id.full_button_mode):
                settings.setMode(Settings.FULL_BUTTON_MODE);

//                backgroundService.setMode(Settings.FULL_BUTTON_MODE);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        settings = SettingsManager.getInstance().getSettings();
        backgroundService = BackgroundService.getInstance();
        setupViewWithSettings();
    }

    void setupViewWithSettings() {
        if (settings.isFlicActivated()) {
            flicSwitch.isChecked();
        }
        if (settings.isLifelogActivated()){
            lifelogSwitch.isChecked();
        }
        if(settings.isMovesActivated()){
            movesSwitch.isChecked();
        }
        switch (settings.getMode()){
            case 0:
                radioGroup.check(R.id.no_mode);
                break;
            case 1:
                radioGroup.check(R.id.binary_mode);
                break;
            case 2:
                radioGroup.check(R.id.full_button_mode);
        }
    }

}
