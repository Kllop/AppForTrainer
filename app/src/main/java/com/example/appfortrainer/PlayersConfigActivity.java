package com.example.appfortrainer;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class PlayersConfigActivity extends Activity{

    private boolean isGoalWhite = false;
    private boolean isGoalBlue = false;
    DisplayMetrics displayMetrics = new DisplayMetrics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_menu);
        EditText textName = (EditText) findViewById(R.id.NameProject);
        String timeStamp = new SimpleDateFormat("dd.MM.yy HH-mm").format(Calendar.getInstance().getTime());
        textName.setText(timeStamp);
        SeekBar seekBarWhite = findViewById(R.id.count_main_player);
        SeekBar seekBarBlue = findViewById(R.id.count_enemy_player);
        setListerCountPlayer(seekBarBlue, seekBarWhite);
        ImageView imageField = findViewById(R.id.imageField);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ViewGroup.LayoutParams paramsFull = imageField.getLayoutParams();

        switch (Settings.LoadMainSceneSettings.typeField){
            case full:
                seekBarBlue.setProgress(6);
                seekBarWhite.setProgress(6);
                imageField.setImageResource(R.mipmap.fields_full);
                paramsFull.height = displayMetrics.heightPixels + (int) TouchConttroler.dipToPixels(150, this);
                break;
            case half:
                seekBarBlue.setProgress(6);
                seekBarWhite.setProgress(5);
                imageField.setImageResource(R.mipmap.fields_1_2);
                paramsFull.height = displayMetrics.heightPixels + (int) TouchConttroler.dipToPixels(150, this);
                break;
            case three:
                seekBarBlue.setProgress(6);
                seekBarWhite.setProgress(5);
                imageField.setImageResource(R.mipmap.fields_1_2_3x3);
                paramsFull.height = displayMetrics.heightPixels + (int) TouchConttroler.dipToPixels(150, this);
                break;
            case four:
                seekBarBlue.setProgress(5);
                setGoalBlue();
                seekBarWhite.setProgress(5);
                imageField.setImageResource(R.mipmap.fields_1_2_4x2);
                paramsFull.height = displayMetrics.heightPixels + (int) TouchConttroler.dipToPixels(150, this);
                break;
        }
        imageField.setLayoutParams(paramsFull);
    }

    @Override
    public void onBackPressed() {
    }

    public void setListerCountPlayer(SeekBar seekBarBlue, SeekBar seekBarWhite){
        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i == seekBar.getMax()){setGoalBlue();}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarWhite.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i == seekBar.getMax()){setGoalWhite();}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void SwitchGoalBlue(View view){
        setGoalBlue();
    }

    public void setGoalBlue() {
        SeekBar seekBar = findViewById(R.id.count_enemy_player);
        if(seekBar.getProgress() == seekBar.getMax() & isGoalBlue){return;}
        isGoalBlue = !isGoalBlue;
        ImageButton imageButton = (ImageButton) findViewById(R.id.switch_goal_blue);
        if(isGoalBlue) {
            imageButton.setImageResource(R.mipmap.goal_blue_yes);
        }else{
            imageButton.setImageResource(R.mipmap.goal_blue_no);
        }
    }

    public void SwitchGoalWhite(View view){
        setGoalWhite();
    }

    public void setGoalWhite() {
        SeekBar seekBar = findViewById(R.id.count_main_player);
        if(seekBar.getProgress() == seekBar.getMax() & isGoalWhite){return;}
        isGoalWhite = !isGoalWhite;
        ImageButton imageButton = (ImageButton) findViewById(R.id.switch_goal_white);
        if(isGoalWhite) {
            imageButton.setImageResource(R.mipmap.goal_white_yes);
        }else{
            imageButton.setImageResource(R.mipmap.goal_white_no);
        }
    }

    public void BackMenu(View view){
        finish();
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

        Settings.LoadMainSceneSettings.NameScene = name.replace(" ", "_");;
        SeekBar mainPlayer = (SeekBar) findViewById(R.id.count_main_player);
        Settings.LoadMainSceneSettings.CountMainPlayer =  mainPlayer.getProgress() + 1;
        SeekBar enemyPlayer = (SeekBar) findViewById(R.id.count_enemy_player);
        Settings.LoadMainSceneSettings.CountEnemyPlayer = enemyPlayer.getProgress() + 1;
        Settings.LoadMainSceneSettings.isGoalMain = isGoalWhite;
        Settings.LoadMainSceneSettings.isGoalEnemy = isGoalBlue;
        Settings.indexFile = -1;
        Settings.isFirstStart = true;

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
