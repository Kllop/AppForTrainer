package com.example.appfortrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

public class ChangeMenuAcivity extends Activity{

    private TextView textMainPlayer;
    private TextView textEnemyPlayer;

    private SeekBar seekBarMain;
    private SeekBar seekBarEnemy;
    private EditText textName;
    private Settings.TypeField currentTypeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_menu);
        textMainPlayer = findViewById(R.id.TextCountMainPlayer);
        textEnemyPlayer = findViewById(R.id.TextCountEnemyPlayer);
        seekBarMain = (SeekBar) findViewById(R.id.ChangeMainCount);
        textName = (EditText) findViewById(R.id.NameProject);
        String timeStamp = new SimpleDateFormat("HH.mm.dd.MM.yy").format(Calendar.getInstance().getTime());
        textName.setText(timeStamp);
        currentTypeField = Settings.TypeField.full;
        SwitchFieldType(currentTypeField, true);

        seekBarMain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textMainPlayer.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarEnemy = (SeekBar) findViewById(R.id.ChangeEnemyCount);
        seekBarEnemy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textEnemyPlayer.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        textMainPlayer.setText(String.valueOf(seekBarMain.getProgress() + 1 ));
        textEnemyPlayer.setText(String.valueOf(seekBarEnemy.getProgress() + 1));
    }
    private void SwitchFieldType(Settings.TypeField typeField, boolean isOn){
        switch (typeField){
            case full:
                ImageButton buttonFull = findViewById(R.id.button_full_field);
                if(isOn){buttonFull.setImageResource(R.drawable.yellow_sq_full_field);}
                else{buttonFull.setImageResource(R.drawable.blue_sq_full_field);}
                break;
            case half:
                ImageButton buttonHalf = findViewById(R.id.button_half_field);
                if(isOn){buttonHalf.setImageResource(R.drawable.yellow_sq_half_field);}
                else{buttonHalf.setImageResource(R.drawable.blue_sq_half_field);}
                break;
            case three:
                ImageButton buttonThree = findViewById(R.id.button_three_field);
                if(isOn){buttonThree.setImageResource(R.drawable.yellow_btn_3x3);}
                else{buttonThree.setImageResource(R.drawable.blue_btn_3x3);}
                break;
            case four:
                ImageButton buttonFour = findViewById(R.id.button_four_field);
                if(isOn){buttonFour.setImageResource(R.drawable.yellow_btn_4x2);}
                else{buttonFour.setImageResource(R.drawable.blue_btn_4x2);}
                break;
            default:
                break;
        }
    }
    private void SwitchField(Settings.TypeField field){
        SwitchFieldType(currentTypeField, false);
        currentTypeField = field;
        SwitchFieldType(currentTypeField, true);
    }
    public void setFullField(View viw){
        SwitchField(Settings.TypeField.full);
    }
    public void setHalfField(View viw){
        SwitchField(Settings.TypeField.half);
    }
    public void setThreeField(View viw){
        SwitchField(Settings.TypeField.three);
    }
    public void setFourField(View viw){
        SwitchField(Settings.TypeField.four);
    }
    public void Next(View view) {
        String name = textName.getText().toString();
        if(name.isEmpty()){
            Toast toast = Toast.makeText(this, "Name is empty", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        for (SaveAndLoadComponent.UintSaveProjectionsClass project :Settings.saveAndLoadComponent.projections.projects) {
            if(project.nameFile == name){
                Toast toast = Toast.makeText(this, "Name already in use. Please choose another one.", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }
        Settings.LoadMainSceneSettings.NameScene = name;
        Settings.LoadMainSceneSettings.CountMainPlayer = seekBarMain.getProgress() + 1;
        Settings.LoadMainSceneSettings.CountEnemyPlayer = seekBarEnemy.getProgress() + 1;
        Settings.LoadMainSceneSettings.typeField = currentTypeField;
        Settings.indexFile = -1;
        textMainPlayer = null;
        textEnemyPlayer = null;
        seekBarMain = null;
        seekBarEnemy = null;
        textName = null;

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
