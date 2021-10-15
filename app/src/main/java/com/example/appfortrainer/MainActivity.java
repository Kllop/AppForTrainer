package com.example.appfortrainer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.example.appfortrainer.paint.PaintConttroler;
import com.example.appfortrainer.paint.PaintConttroler.TypePaint;
import com.hbisoft.hbrecorder.HBRecorder;
import com.hbisoft.hbrecorder.HBRecorderListener;

import java.io.File;

public class MainActivity extends Activity implements HBRecorderListener
{
    DisplayMetrics displayMetrics = new DisplayMetrics();
    public VibrationComponent vib;
    public TouchConttroler touchConttroler;
    public PaintConttroler paintConttroler;
    public AnimationConttroler animationConttroler;
    private TypePaint currentPaint;
    private HBRecorder hbRecorder;
    private boolean onRenderer = false;
    private static final int SCREEN_RECORD_REQUEST_CODE = 100;

    public final class IntRef{int index = 0;}

    /*
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if(onRenderer) {
                hbRecorder.resumeScreenRecording(); touchConttroler.animationConttroler.OnResumeAnimation();}
        } else{
            if(onRenderer) {
            hbRecorder.pauseScreenRecording(); touchConttroler.animationConttroler.OnPauseAnimation();}
        }
    }
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_main);
        ConstraintLayout paintScene = (ConstraintLayout) findViewById(R.id.paint_parent);
        ConstraintLayout numberScene = (ConstraintLayout) findViewById(R.id.player_number_layout);
        TextView text = findViewById(R.id.FrameCounter);
        ImageButton imageButton = findViewById(R.id.play_button);
        vib = new VibrationComponent(this);
        paintConttroler = new PaintConttroler(this, paintScene);
        animationConttroler = new AnimationConttroler(text, imageButton, paintConttroler);
        touchConttroler = new TouchConttroler(vib, this, text, animationConttroler, numberScene);
        paintConttroler.setAnimationConttroler(animationConttroler);
        SpawnObjectComponent spawnObjectComponent = new SpawnObjectComponent();
        ConstraintLayout playerParent = (ConstraintLayout) findViewById(R.id.player_parent);
        AnimationConttroler.ResetIterator();
        Settings.ResetSettings();
        if(Settings.indexFile == -1) {
            IntRef index = new IntRef();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int positionY = displayMetrics.heightPixels - (int) TouchConttroler.dipToPixels(125, this);
            spawnObjectComponent.SpawnNewPlayer(positionY, Settings.LoadMainSceneSettings.CountMainPlayer,
                    true, this, playerParent, displayMetrics, touchConttroler, index);
            positionY = (int) (TouchConttroler.dipToPixels(125, this) - TouchConttroler.dipToPixels(75, this));
            spawnObjectComponent.SpawnNewPlayer(positionY, Settings.LoadMainSceneSettings.CountEnemyPlayer,
                    false, this, playerParent, displayMetrics, touchConttroler, index);
            spawnObjectComponent.SpawnNewBall(this, playerParent, displayMetrics, touchConttroler, index);
        } else{
            spawnObjectComponent.SpawnOldPlayer(this, touchConttroler, playerParent);
            animationConttroler.ResetPlayerPosition();
        }
        UpdateFrameCounter();
        hbRecorder = new HBRecorder(this, this);
        ImageView field = (ImageView) findViewById(R.id.full_filed);
        spawnObjectComponent.SpawnField(field, displayMetrics, this);
    }

    public void onClickStartAnimation(View view) {
        if(Settings.isPaint){return;}
        if(Settings.isPlayAnimation){
            animationConttroler.OnStopAnimation();
            UpdateAnimationButton(false);
        } else {
            animationConttroler.OnStartAnimation();
            UpdateAnimationButton(true);
        }
    }

    public void OnClickNextFrame(View view){
        if(Settings.isPaint | Settings.isPlayAnimation){return;}
        AnimationConttroler.ietterator.NextValue();
        UpdateFrameCounter();
        animationConttroler.ResetPlayerPosition();
    }

    public void OnClickBackFrame(View view){
        if(Settings.isPaint | Settings.isPlayAnimation){return;}
        AnimationConttroler.ietterator.BackValue();
        UpdateFrameCounter();
        animationConttroler.ResetPlayerPosition();
    }

    public void UpdateFrameCounter() {
        TextView FrameCounter = findViewById(R.id.FrameCounter);
        FrameCounter.setText(String.valueOf(AnimationConttroler.ietterator.GetValue() + 1)
                + "/" + String.valueOf(FrameBuffer.Frames.size() + 1));
    }

    public void UpdateAnimationButton(boolean value) {
        ImageButton PlayAnimationButton = (ImageButton) findViewById(R.id.play_button);
        if(value) PlayAnimationButton.setImageResource(R.drawable.round_blue_stop);
        else PlayAnimationButton.setImageResource(R.drawable.round_blue_play);
    }

    @Override
    public void HBRecorderOnStart() {
        onRenderer = true;
        animationConttroler.OnStartAnimation(hbRecorder);
    }

    @Override
    public void HBRecorderOnComplete() {
        //hbRecorder.
        SetVisibleAllUI(true);
        OpenRenderMenu();
        onRenderer = false;
        if(hbRecorder.getFilePath() != null) { new MediaScaner(this, hbRecorder.getFilePath()); }
    }

    @Override
    public void HBRecorderOnError(int errorCode, String reason) {
        SetVisibleAllUI(true);
        OpenRenderMenu();
        onRenderer = false;
        touchConttroler.animationConttroler.OnStopAnimation();
    }
    public void startRecordingScreen(View view) {
        if(Settings.isPlayAnimation | Settings.isPaint | FrameBuffer.Frames.size() == 0){return;}
        InitRecording();
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = mediaProjectionManager != null ? mediaProjectionManager.createScreenCaptureIntent() : null;
        startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                SetVisibleAllUI(false);
                CloseRenderMenu();
                hbRecorder.startScreenRecording(data, resultCode, this);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void InitRecording() {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/BeStarBoard");
        if (!folder.exists()){folder.mkdirs();}
        hbRecorder.setOutputPath(folder.getPath());
        hbRecorder.isAudioEnabled(false);
        hbRecorder.setFileName(Settings.LoadMainSceneSettings.NameScene);
        hbRecorder.enableCustomSettings();
        hbRecorder.setScreenDimensions(1080,1920);
        hbRecorder.setVideoFrameRate(30);
        hbRecorder.setVideoEncoder("H264");
    }
    private void SetVisibleAllUI(boolean visible) {
        ConstraintLayout ui = (ConstraintLayout) findViewById(R.id.ui_layout);
        if(visible) ui.setVisibility(View.VISIBLE);
        else ui.setVisibility(View.INVISIBLE);
    }
    public void onRecordingStart(View view) {
        ImageButton button = (ImageButton) view;
        if(Settings.isRecording){Settings.isRecording = false; button.setImageResource(R.drawable.round_blue_rec_disabled);}
        else{
            if(FrameBuffer.PlayerPositionInFrame == null){animationConttroler.SaveAllPlayerPosition();}
            Settings.isRecording = true; button.setImageResource(R.drawable.round_blue_rec);
        }
    }
    public void OnCreateLine(View view) {
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        ImageButton button = (ImageButton) view;
        if(Settings.isPaint){
            if(currentPaint != TypePaint.BaseLine){return;}
            button.setImageResource(R.drawable.blue_sq_arr_solid);
            paintConttroler.StopPaint();
        } else {
            button.setImageResource(R.drawable.yellow_sq_arr_solid);
            paintConttroler.OnPaintLine();
            currentPaint = TypePaint.BaseLine;
        }
    }
    public void OnCreateDottedLine(View view) {
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        ImageButton button = (ImageButton) view;
        if(Settings.isPaint){
            if(currentPaint != TypePaint.DottedLine){return;}
            button.setImageResource(R.drawable.blue_sq_arr_doted);
            paintConttroler.StopPaint();
        } else {
            button.setImageResource(R.drawable.yellow_sq_arr_doted);
            paintConttroler.OnPaintDottedLine();
            currentPaint = TypePaint.DottedLine;
        }
    }
    public void OnCreatePencil(View view) {
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        ImageButton button = (ImageButton) view;
        if(Settings.isPaint){
            if(currentPaint != TypePaint.Pencil){return;}
            button.setImageResource(R.drawable.blue_sq_marker);
            paintConttroler.StopPaint();
        } else {
            button.setImageResource(R.drawable.yellow_sq_marker);
            paintConttroler.OnPaintPenсil();
            currentPaint = TypePaint.Pencil;
        }
    }
    public void OnCreateText(View view){
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        ImageButton button = (ImageButton) view;
        if(Settings.isPaint){
            if(currentPaint != TypePaint.Text){return;}
            button.setImageResource(R.drawable.blue_sq_text_block);
            paintConttroler.StopPaint();
        } else {
            button.setImageResource(R.drawable.yellow_sq_text_block);
            paintConttroler.OnPaintText();
            currentPaint = TypePaint.Text;
        }
    }

    public void ClearAll(View view) {
        if(!Settings.isRecording | Settings.isPlayAnimation | Settings.isPaint){return;}
        FrameBuffer.Frames.clear();
        animationConttroler.ietterator.SetFirstValue();
        animationConttroler.ietterator.SetMaxValue(FrameBuffer.Frames.size());
        animationConttroler.ResetPlayerPosition();
        UpdateFrameCounter();
    }
    public void EventOpenRenderMenu(View view){OpenRenderMenu();}
    public void OpenRenderMenu(){
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.render_menu);
        menu.setVisibility(View.VISIBLE);
    }
    public void EventCloseRenderMenu(View view){CloseRenderMenu();}
    public void CloseRenderMenu(){
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.render_menu);
        menu.setVisibility(View.GONE);
    }

    public void SaveFile(View view){Settings.saveAndLoadComponent.SaveInFileProjections(this);}

    public void OnMainMenu(View view) {
        FrameBuffer.ResetBuffer();
        touchConttroler = null;
        paintConttroler = null;
        animationConttroler = null;
        Settings.ResetSettings();
        Settings.playersForIndexWrite.clear();
        Settings.playersForIndexRead.clear();
        finish();
    }
    public void ToLastFrame(View view){
        if(Settings.isPaint | Settings.isPlayAnimation){return;}
        if(FrameBuffer.Frames.size() == 0){return;}
        AnimationConttroler.ietterator.SetLastValue();
        animationConttroler.ResetPlayerPosition();
        UpdateFrameCounter();
    }
    public void ToFirstFrame(View view){
        if(Settings.isPaint | Settings.isPlayAnimation){return;}
        if(FrameBuffer.Frames.size() == 0){return;}
        AnimationConttroler.ietterator.SetFirstValue();
        animationConttroler.ResetPlayerPosition();
        UpdateFrameCounter();
    }

    public void WatchVideo(View view){
        if(hbRecorder.getFilePath() == null){return;}
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(hbRecorder.getFilePath()));
        intent.putExtra("force_fullscreen",true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setDataAndType(Uri.parse(hbRecorder.getFilePath()), "video/mp4");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
        }
    }
    public void ShareVideo(View view){
        if(hbRecorder.getFilePath() == null){return;}
        File videoFile = new File(hbRecorder.getFilePath());
        Uri videoURI =FileProvider.getUriForFile(this, getPackageName(), videoFile);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, videoURI);
        shareIntent.setType("video/mp4");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }

    public void disablePlayerNumberMenu(View view){
        touchConttroler.currentPlayerNumber = null;
        findViewById(R.id.player_number_layout).setVisibility(View.GONE);
    }

    public void setNumberPlayer(View view){
        Button button = (Button) view;
        int number = Integer.parseInt(String.valueOf(button.getText()));
        int index = (int) Settings.playersForIndexWrite.get(touchConttroler.currentPlayerNumber);
        FrameBuffer.PlayerInformation informationPlayer = null;
        for(FrameBuffer.PlayerInformation information : FrameBuffer.playerInformations){
            if(information.Index == index){informationPlayer = information; break;}
        }
        if(informationPlayer == null){return;}
        int image;
        switch (informationPlayer.typePlayer){
            case mainPlayer:
                image = touchConttroler.getPlayerImageForNumber(number, true);
                break;
            case enemyPlayer:
                image = touchConttroler.getPlayerImageForNumber(number, false);
                break;
            default:
                image = -1;
                break;
        }
        if(image == -1){return;}
        touchConttroler.currentPlayerNumber.setImageResource(image);
        findViewById(R.id.player_number_layout).setVisibility(View.GONE);
    }
}