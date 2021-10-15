package com.example.appfortrainer;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.HashMap;

public class SpawnObjectComponent {

    private final static int SIZE_PLAYER_DIP = 50;
    private final static int SIZE_BALL_DIP = 40;

    public void SpawnNewPlayer(float pY, int Count,  boolean IsMainPlayer , Context context, ConstraintLayout mainScene, DisplayMetrics dMetrics, TouchConttroler touch, MainActivity.IntRef Index) {
        final float LengthSpawn = dMetrics.widthPixels / 1.5f;
        for(int i = 0; i < Count; i++) {
            float positionX = (dMetrics.widthPixels-LengthSpawn)/2 + 50 + (LengthSpawn/Count * i);
            ConstraintLayout mainLayout = mainScene;
            ImageButton player = new ImageButton(context);
            if(IsMainPlayer)player.setImageResource(getImageWhitePlayerFromIndex(i + 1));
            else player.setImageResource(getImageBluePlayerFromIndex(i + 1));
            int sizePlayer = (int) TouchConttroler.dipToPixels(SIZE_PLAYER_DIP, context);
            ConstraintLayout.LayoutParams imageViewLayoutParams = new ConstraintLayout.LayoutParams(sizePlayer , sizePlayer);
            player.setLayoutParams(imageViewLayoutParams);
            player.setX(positionX);
            player.setY(pY);
            player.setBackgroundColor(0x00FFFFFF);
            player.setScaleType(ImageView.ScaleType.FIT_CENTER);
            player.setOnTouchListener(touch.TouchPlayer);

            mainLayout.addView(player);

            Settings.playersForIndexWrite.put(player, Index.index);
            Settings.playersForIndexRead.put(Index.index, player);

            FrameBuffer.PlayerInformation playerInformation = new FrameBuffer.PlayerInformation();
            playerInformation.Index = Index.index;
            if(IsMainPlayer) playerInformation.typePlayer = FrameBuffer.TypePlayer.mainPlayer;
            else playerInformation.typePlayer = FrameBuffer.TypePlayer.enemyPlayer;
            playerInformation.Number = i + 1;
            playerInformation.Name = "Name";
            FrameBuffer.playerInformations.add(playerInformation);
            Index.index++;
        }
    }

    public int getImageBluePlayerFromIndex(int index){
        switch (index){
            case 1:
                return R.drawable.blue_player_1;
            case 2:
                return R.drawable.blue_player_2;
            case 3:
                return R.drawable.blue_player_3;
            case 4:
                return R.drawable.blue_player_4;
            case 5:
                return R.drawable.blue_player_5;
            case 6:
                return R.drawable.blue_player_6;
            case 7:
                return R.drawable.blue_player_7;
            case 8:
                return R.drawable.blue_player_8;
            case 9:
                return R.drawable.blue_player_9;
            case 10:
                return R.drawable.blue_player_10;
            case 11:
                return R.drawable.blue_player_11;
            case 12:
                return R.drawable.blue_player_12;
            case 13:
                return R.drawable.blue_player_13;
        };
        return -1;
    }

    public int getImageWhitePlayerFromIndex(int index){
        switch (index){
            case 1:
                return R.drawable.white_player_1;
            case 2:
                return R.drawable.white_player_2;
            case 3:
                return R.drawable.white_player_3;
            case 4:
                return R.drawable.white_player_4;
            case 5:
                return R.drawable.white_player_5;
            case 6:
                return R.drawable.white_player_6;
            case 7:
                return R.drawable.white_player_7;
            case 8:
                return R.drawable.white_player_8;
            case 9:
                return R.drawable.white_player_9;
            case 10:
                return R.drawable.white_player_10;
            case 11:
                return R.drawable.white_player_11;
            case 12:
                return R.drawable.white_player_12;
            case 13:
                return R.drawable.white_player_13;
        };
        return -1;
    }
    public void SpawnField(ImageView field,  DisplayMetrics metrics, Context context){
        switch (Settings.LoadMainSceneSettings.typeField){
            case full:
                field.setImageResource(R.drawable.full_field);
                ViewGroup.LayoutParams paramsFull = field.getLayoutParams();
                paramsFull.height = metrics.heightPixels - (int) TouchConttroler.dipToPixels(20, context);
                paramsFull.width = metrics.widthPixels - (int) TouchConttroler.dipToPixels(140, context);
                field.setLayoutParams(paramsFull);
                return;
            case half:
                field.setImageResource(R.drawable.field_half);
                ViewGroup.LayoutParams paramsHalf = field.getLayoutParams();
                paramsHalf.height = metrics.heightPixels - (int) TouchConttroler.dipToPixels(20, context);
                paramsHalf.width = metrics.widthPixels - (int) TouchConttroler.dipToPixels(260, context);
                field.setLayoutParams(paramsHalf);
                break;
            case three:
                field.setImageResource(R.drawable.field_preset_3x3);
                ViewGroup.LayoutParams paramsThree = field.getLayoutParams();
                paramsThree.height = metrics.heightPixels - (int) TouchConttroler.dipToPixels(20, context);
                paramsThree.width = metrics.widthPixels - (int) TouchConttroler.dipToPixels(260, context);
                field.setLayoutParams(paramsThree);
                break;
            case four:
                field.setImageResource(R.drawable.field_preset_4x2);
                ViewGroup.LayoutParams paramsFour = field.getLayoutParams();
                paramsFour.height = metrics.heightPixels - (int) TouchConttroler.dipToPixels(20, context);
                paramsFour.width = metrics.widthPixels - (int) TouchConttroler.dipToPixels(260, context);
                field.setLayoutParams(paramsFour);
                break;
            default:
                break;
        }
    }


    public void SpawnNewBall(Context context, ConstraintLayout mainScene, DisplayMetrics dMetrics, TouchConttroler touch, MainActivity.IntRef Index) {
        ConstraintLayout mainLayout = mainScene;
        ImageButton player = new ImageButton(context);
        player.setImageResource(R.drawable.ball);
        int sizeBall = (int) TouchConttroler.dipToPixels(SIZE_BALL_DIP, context);
        ConstraintLayout.LayoutParams imageViewLayoutParams = new ConstraintLayout.LayoutParams(sizeBall , sizeBall);
        player.setLayoutParams(imageViewLayoutParams);
        player.setX(dMetrics.widthPixels/2 - sizeBall/2);
        player.setY(dMetrics.heightPixels/2 - sizeBall/2);
        player.setBackgroundColor(0x00FFFFFF);
        player.setScaleType(ImageView.ScaleType.FIT_CENTER);
        player.setOnTouchListener(touch.TouchBall);

        mainLayout.addView(player);
        Settings.ball = player;
        Settings.playersForIndexWrite.put(player, Index.index);
        Settings.playersForIndexRead.put(Index.index, player);

        FrameBuffer.PlayerInformation playerInformation = new FrameBuffer.PlayerInformation();
        playerInformation.Index = Index.index;
        playerInformation.typePlayer = FrameBuffer.TypePlayer.ball;
        playerInformation.Number = 0;
        playerInformation.Name = "Name";
        FrameBuffer.playerInformations.add(playerInformation);
        Index.index++;
    }


    public void SpawnOldPlayer(Context context, TouchConttroler touch, ConstraintLayout mainLayout){
        Settings.playersForIndexWrite = new HashMap();
        Settings.playersForIndexRead = new HashMap();
        FrameBuffer.PlayerPosition playerPosition = FrameBuffer.PlayerPositionInFrame;
        for(FrameBuffer.PlayerInformation playerInformation : FrameBuffer.playerInformations){
            int index = playerInformation.Index;
            ImageButton player = new ImageButton(context);
            switch(playerInformation.typePlayer)
            {
                case ball:
                    //player.setImageResource(R.drawable.ball);
                    break;
                case mainPlayer:
                    player.setImageResource(getImageWhitePlayerFromIndex(playerInformation.Index));
                    break;
                case enemyPlayer:
                    player.setImageResource(getImageBluePlayerFromIndex(playerInformation.Index));
                    break;
            }
            ConstraintLayout.LayoutParams imageViewLayoutParams;
            if(playerInformation.typePlayer == FrameBuffer.TypePlayer.ball)
            {imageViewLayoutParams = new ConstraintLayout.LayoutParams(150, 150);
                player.setOnTouchListener(touch.TouchBall);
            } else{ imageViewLayoutParams = new ConstraintLayout.LayoutParams(200, 200);
                player.setOnTouchListener(touch.TouchPlayer);}
            player.setLayoutParams(imageViewLayoutParams);
            player.setBackgroundColor(0x00FFFFFF);
            player.setScaleType(ImageView.ScaleType.FIT_CENTER);

            mainLayout.addView(player);
            Settings.playersForIndexWrite.put((View) player, index);
            Settings.playersForIndexRead.put(index, (View)player);
        }
    }

}
