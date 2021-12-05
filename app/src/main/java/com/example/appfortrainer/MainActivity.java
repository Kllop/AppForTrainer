package com.example.appfortrainer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
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
import java.util.Set;

public class MainActivity extends Activity implements HBRecorderListener
{
    DisplayMetrics displayMetrics = new DisplayMetrics();
    public VibrationComponent vib;
    public TouchConttroler touchConttroler;
    public PaintConttroler paintConttroler;
    public AnimationConttroler animationConttroler;
    private HBRecorder hbRecorder;
    private boolean onRenderer = false;
    private static final int SCREEN_RECORD_REQUEST_CODE = 100;
    private Delegate delegate;
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
        ConstraintLayout playerParent = (ConstraintLayout) findViewById(R.id.player_parent);
        TextView text = findViewById(R.id.FrameCounter);
        ImageButton imageButton = findViewById(R.id.play_button);
        vib = new VibrationComponent(this);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        paintConttroler = new PaintConttroler(this, paintScene);
        animationConttroler = new AnimationConttroler(text, imageButton, paintConttroler);
        touchConttroler = new TouchConttroler(vib, text, animationConttroler, numberScene, displayMetrics);
        paintConttroler.setAnimationConttroler(animationConttroler);
        SpawnObjectComponent spawnObjectComponent = new SpawnObjectComponent();
        if(Settings.isFirstStart){
            AnimationConttroler.ResetIterator();
        }
        if(Settings.indexFile == -1 & Settings.isFirstStart) {
            int index = 0;
            int positionYWhite = 0, positionYBlue = 0;
            switch (Settings.LoadMainSceneSettings.typeField){
                case full:
                    positionYWhite = displayMetrics.heightPixels - (int) TouchConttroler.dipToPixels(125, this);
                    positionYBlue = (int) (TouchConttroler.dipToPixels(125, this) - TouchConttroler.dipToPixels(75, this));
                    break;
                case half:
                    positionYBlue = displayMetrics.heightPixels - (int) TouchConttroler.dipToPixels(125, this);
                    positionYWhite = (int) (TouchConttroler.dipToPixels(125, this) - TouchConttroler.dipToPixels(75, this));
                    break;
                case three:
                    positionYBlue = displayMetrics.heightPixels - (int) TouchConttroler.dipToPixels(125, this);
                    positionYWhite = (int) (TouchConttroler.dipToPixels(125, this) - TouchConttroler.dipToPixels(75, this));
                    break;
                case four:
                    positionYBlue = displayMetrics.heightPixels - (int) TouchConttroler.dipToPixels(125, this);
                    positionYWhite = (int) (TouchConttroler.dipToPixels(125, this) - TouchConttroler.dipToPixels(75, this));
                    break;
            }

            spawnObjectComponent.SpawnNewPlayer(positionYWhite, Settings.LoadMainSceneSettings.CountMainPlayer,
                    true, this, playerParent, displayMetrics, touchConttroler, index);
            index += Settings.LoadMainSceneSettings.CountMainPlayer;
            spawnObjectComponent.SpawnNewPlayer(positionYBlue, Settings.LoadMainSceneSettings.CountEnemyPlayer,
                    false, this, playerParent, displayMetrics, touchConttroler, index);
            index += Settings.LoadMainSceneSettings.CountMainPlayer;
            spawnObjectComponent.SpawnNewBall(this, playerParent, displayMetrics, touchConttroler, index);
            Settings.isFirstStart = false;
        } else{
            spawnObjectComponent.SpawnOldPlayer(this, touchConttroler, playerParent, displayMetrics);
            animationConttroler.ResetPlayerPosition();
        }
        UpdateFrameCounter();
        hbRecorder = new HBRecorder(this, this);
        ImageView fieldLogo = (ImageView) findViewById(R.id.logo_field);
        ImageView field = (ImageView) findViewById(R.id.full_filed);
        spawnObjectComponent.SpawnField(field, fieldLogo, displayMetrics, this);
        touchConttroler.setFieldInformation(field, displayMetrics);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "set players to initial positions and press record", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClickStartAnimation(View view) {
        if(FrameBuffer.Frames.size() == 0){return;}
        if(Settings.isPaint){StopPaint();}
        if(Settings.isPlayAnimation){
            animationConttroler.OnStopAnimation();
            UpdateAnimationButton(false);
        } else {
            animationConttroler.OnStartAnimation();
            UpdateAnimationButton(true);
        }
    }

    public void OnClickNextFrame(View view){
        if(Settings.isPlayAnimation){return;}
        if(Settings.isPaint){StopPaint();}
        AnimationConttroler.ietterator.NextValue();
        UpdateFrameCounter();
        animationConttroler.ResetPlayerPosition();
    }

    public void OnClickBackFrame(View view){
        if(Settings.isPlayAnimation){return;}
        if(Settings.isPaint){StopPaint();}
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
        if(value) PlayAnimationButton.setImageResource(R.mipmap.round_blue_stop);
        else PlayAnimationButton.setImageResource(R.mipmap.round_blue_play);
    }

    @Override
    public void HBRecorderOnStart() {
        onRenderer = true;
        animationConttroler.OnStartAnimation(hbRecorder);
    }

    @Override
    public void HBRecorderOnComplete() {
        SetVisibleAllUI(true);
        OpenRenderMenu();
        onRenderer = false;
        if(hbRecorder.getFilePath() != null) { new MediaScaner(this, hbRecorder.getFilePath());}
        delegate.Call();
    }

    @Override
    public void HBRecorderOnError(int errorCode, String reason) {
        SetVisibleAllUI(true);
        OpenRenderMenu();
        onRenderer = false;
        touchConttroler.animationConttroler.OnStopAnimation();
    }
    public void startRecordingScreen(View view) {
        delegate = new Delegate();
        CallStartScreenRecording();
    }

    public void CallStartScreenRecording(){
        if(Settings.isPlayAnimation | FrameBuffer.Frames.size() == 0){return;}
        if(Settings.isPaint){StopPaint();}
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hbRecorder.startScreenRecording(data, resultCode, MainActivity.this);
                    }
                }, 100);

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
        if(Settings.isRecording){Settings.isRecording = false; button.setImageResource(R.mipmap.round_blue_rec);}
        else{
            if(FrameBuffer.PlayerPositionInFrame == null){animationConttroler.SaveAllPlayerPosition();}
            Settings.isRecording = true; button.setImageResource(R.mipmap.round_blue_rec_disabled);
        }
    }

    private void InitPaint(){findViewById(R.id.paint_parent).setTranslationZ(4);}

    private void StopPaint(){
        ImageButton button;
        button = (ImageButton) findViewById(R.id.button_base_line);
        button.setImageResource(R.drawable.blue_sq_arr_solid);
        button = (ImageButton) findViewById(R.id.button_dotted_line);
        button.setImageResource(R.drawable.blue_sq_arr_doted);
        button = (ImageButton) findViewById(R.id.button_pencil);
        button.setImageResource(R.drawable.blue_sq_marker);
        button = (ImageButton) findViewById(R.id.button_create_text);
        button.setImageResource(R.drawable.blue_sq_text_block);
        paintConttroler.StopPaint();
    }

    public void OnCreateLine(View view) {
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        ImageButton button = (ImageButton) view;
        if(paintConttroler.CurrentPaintType == TypePaint.BaseLine){StopPaint(); return;}
        if(Settings.isPaint){StopPaint();}
        button.setImageResource(R.drawable.yellow_sq_arr_solid);
        paintConttroler.OnPaintLine();
        InitPaint();
    }

    public void OnCreateDottedLine(View view) {
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        ImageButton button = (ImageButton) view;
        if(paintConttroler.CurrentPaintType == TypePaint.DottedLine){StopPaint(); return;}
        if(Settings.isPaint){StopPaint();}
        button.setImageResource(R.drawable.yellow_sq_arr_doted);
        paintConttroler.OnPaintDottedLine();
        InitPaint();
    }
    public void OnCreatePencil(View view) {
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        ImageButton button = (ImageButton) view;
        if(paintConttroler.CurrentPaintType == TypePaint.Pencil){StopPaint(); return;}
        if(Settings.isPaint){StopPaint();}
        button.setImageResource(R.drawable.yellow_sq_marker);
        paintConttroler.OnPaintPenсil();
        InitPaint();
    }
    public void OnCreateText(View view){
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        ImageButton button = (ImageButton) view;
        if(paintConttroler.CurrentPaintType == TypePaint.Text){StopPaint(); return;}
        if(Settings.isPaint){StopPaint();}
        button.setImageResource(R.drawable.yellow_sq_text_block);
        paintConttroler.OnPaintText();
        InitPaint();
    }

    public void OnCreateEraser(View view) {
        if(!Settings.isRecording | Settings.isPlayAnimation | Settings.isPaint){return;}
        paintConttroler.OnPaintEraser();
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

    public void SaveFile(View view){
        if(Settings.isPaint | Settings.isPlayAnimation){return;}
        if(FrameBuffer.Frames.size() == 0){return;}
        Settings.saveAndLoadComponent.SaveScene(this, Settings.LoadMainSceneSettings.NameScene);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

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
        if(Settings.isPlayAnimation){return;}
        if(Settings.isPaint){StopPaint();}
        if(FrameBuffer.Frames.size() == 0){return;}
        AnimationConttroler.ietterator.SetLastValue();
        animationConttroler.ResetPlayerPosition();
        UpdateFrameCounter();
    }
    public void ToFirstFrame(View view){
        if(Settings.isPlayAnimation){return;}
        if(Settings.isPaint){StopPaint();}
        if(FrameBuffer.Frames.size() == 0){return;}
        AnimationConttroler.ietterator.SetFirstValue();
        animationConttroler.ResetPlayerPosition();
        UpdateFrameCounter();
    }
    /*
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
     */
    public void ShareVideo(View view){
        CallShareVideo();
    }
    public void CallShareVideo(){
        if(hbRecorder.getFilePath() == null){
            delegate = new Delegate(){
            @Override
            public void Call(){CallShareVideo();}
        };
            CallStartScreenRecording();
            return;
        }
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
        FrameBuffer.PlayerInformation otherPlayer = null;
        if(number == 1 | number == 12){
            for(FrameBuffer.PlayerInformation information : FrameBuffer.playerInformations){
                if(information.Index == index){informationPlayer = information; break;}
            }
            for(FrameBuffer.PlayerInformation information : FrameBuffer.playerInformations) {
                if ((information.Number == 1 | information.Number == 12) & information.typePlayer == informationPlayer.typePlayer) { otherPlayer = information;break;
                }
            }
        } else{
            for(FrameBuffer.PlayerInformation information : FrameBuffer.playerInformations){
                if(information.Index == index){informationPlayer = information; break;}
            }
            for(FrameBuffer.PlayerInformation information : FrameBuffer.playerInformations){
                if(information.Number == number & information.typePlayer == informationPlayer.typePlayer){ otherPlayer = information; break;}
            }
        }
        if(informationPlayer == null){return;}
        int image = -1, otherImage = -1;
        switch (informationPlayer.typePlayer){
            case mainPlayer:
                image = touchConttroler.getPlayerImageForNumber(number, true);
                if(otherPlayer != null){
                    otherImage = touchConttroler.getPlayerImageForNumber(informationPlayer.Number, true);
                }
                break;
            case enemyPlayer:
                image = touchConttroler.getPlayerImageForNumber(number, false);
                if(otherPlayer != null){
                    otherImage = touchConttroler.getPlayerImageForNumber(informationPlayer.Number, false);
                }
                break;
            default:
                break;
        }
        int intOldPlayerNumber = informationPlayer.Number;
        if(image != -1){
            ImageButton player = (ImageButton) Settings.playersForIndexRead.get(informationPlayer.Index);
            player.setImageResource(image);
            informationPlayer.Number = number;
        }
        if(otherImage != -1){
            ImageButton player = (ImageButton) Settings.playersForIndexRead.get(otherPlayer.Index);
            player.setImageResource(otherImage);
            otherPlayer.Number = intOldPlayerNumber;
        }
        findViewById(R.id.player_number_layout).setVisibility(View.GONE);
    }
}
