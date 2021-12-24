package we.polo.appfortrainer;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


public class SettingsActivity extends Activity {
    public final String activeColor = "#9C9C9C";
    public final String deactiveColor = "#103033";


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
        Switch vib = (Switch) findViewById(R.id.vibration);
        vib.setChecked(Settings.SceneSettings.Vibration);
        vib.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Settings.SceneSettings.Vibration = b;
                SaveSettings();
            }
        });

        float animationSpeed = Settings.SceneSettings.AnimationSpeed;
        if(animationSpeed == 1.0f){
            Button button_1 = findViewById(R.id.button_sped_1);
            SetColorActiveButton(button_1);
        }
        else if(animationSpeed == 1.5f){
            Button button_2 = findViewById(R.id.button_sped_2);
            SetColorActiveButton(button_2);
        }
        else if (animationSpeed == 2.0f){
            Button button_3 = findViewById(R.id.button_sped_3);
            SetColorActiveButton(button_3);
        }
    }
    public void SaveSettings()
    {
        Settings.saveAndLoadComponent.SaveSettings(this);
    }

    public void BackMainMenu(View view)
    {
        finish();
    }

    @Override
    public void onBackPressed() {
    }

    public void button_speed_1(View view){
        Settings.SceneSettings.AnimationSpeed = 1.0f;
        SaveSettings();
        SetColorActiveButton(view);
    }
    public void button_speed_2(View view){
        Settings.SceneSettings.AnimationSpeed = 1.5f;
        SaveSettings();
        SetColorActiveButton(view);
    }
    public void button_speed_3(View view){
        Settings.SceneSettings.AnimationSpeed = 2.0f;
        SaveSettings();
        SetColorActiveButton(view);
    }

    private void SetColorActiveButton(View view){
        Button button_1 = findViewById(R.id.button_sped_1);
        button_1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(deactiveColor)));
        Button button_2 = findViewById(R.id.button_sped_2);
        button_2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(deactiveColor)));
        Button button_3 = findViewById(R.id.button_sped_3);
        button_3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(deactiveColor)));
        view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(activeColor)));
    }

}
