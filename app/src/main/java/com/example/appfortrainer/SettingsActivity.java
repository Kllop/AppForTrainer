package com.example.appfortrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;


public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Switch playerMove = (Switch) findViewById(R.id.player_move);
        playerMove.setChecked(Settings.SceneSettings.MovePlayerInFirstFrame);
        playerMove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Settings.SceneSettings.MovePlayerInFirstFrame = b;
                SaveSettings();
            }
        });
        Switch freeAngle = (Switch) findViewById(R.id.free_angle);
        freeAngle.setChecked(Settings.SceneSettings.FreeAngleAttach);
        freeAngle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Settings.SceneSettings.FreeAngleAttach = b;
                SaveSettings();
            }
        });
    }
    public void SaveSettings()
    {
        Settings.saveAndLoadComponent.SaveInFileSettings(this);
    }

    public void BackMainMenu(View view)
    {
        Intent intent = new Intent(this, MainMenuAcitivity.class);
        startActivity(intent);
    }

}
