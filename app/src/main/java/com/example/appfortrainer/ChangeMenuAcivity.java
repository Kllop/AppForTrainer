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
import java.util.Map;
import java.util.Set;

public class ChangeMenuAcivity extends Activity{

    private Settings.TypeField currentTypeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_menu);
        EditText textName = (EditText) findViewById(R.id.NameProject);
        String timeStamp = new SimpleDateFormat("HH.mm.dd.MM.yy").format(Calendar.getInstance().getTime());
        textName.setText(timeStamp);
        currentTypeField = Settings.TypeField.full;
        SwitchFieldType(currentTypeField, true);
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
        EditText textName = (EditText) findViewById(R.id.NameProject);
        String name = textName.getText().toString();
        if(name.isEmpty()){
            Toast toast = Toast.makeText(this, "Name is empty", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        for (Map.Entry<Integer, String> entry : Settings.saveAndLoadComponent.projectionsInfo.entrySet()){
            if(entry.getValue() == name){
                Toast toast = Toast.makeText(this, "Name already in use. Please choose another one.", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }
        FrameBuffer.ResetBuffer();
        Settings.ResetSettings();
        Settings.LoadMainSceneSettings.NameScene = name;
        SeekBar mainPlayer = (SeekBar) findViewById(R.id.count_main_player);
        Settings.LoadMainSceneSettings.CountMainPlayer =  mainPlayer.getProgress() + 1;
        SeekBar enemyPlayer = (SeekBar) findViewById(R.id.count_enemy_player);
        Settings.LoadMainSceneSettings.CountEnemyPlayer = enemyPlayer.getProgress() + 1;
        Settings.LoadMainSceneSettings.typeField = currentTypeField;
        Settings.indexFile = -1;

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
