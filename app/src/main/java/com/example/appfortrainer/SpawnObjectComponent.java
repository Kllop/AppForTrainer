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

    public void SpawnNewPlayer(float pY, int Count,  boolean IsMainPlayer , Context context, ConstraintLayout mainScene, DisplayMetrics dMetrics, TouchConttroler touch, Integer Index) {
        final float LengthSpawn = dMetrics.widthPixels / 1.5f;
        int startValue = 0;
        if(IsMainPlayer){startValue = Settings.LoadMainSceneSettings.isGoalMain ? 0 : 1;}
        else{startValue = Settings.LoadMainSceneSettings.isGoalEnemy ? 0 : 1;}
        if(startValue == 1){Count++;}
        for(int i = startValue; i < Count; i++) {
            float positionX = (dMetrics.widthPixels-LengthSpawn)/2 + 50 + (LengthSpawn/Count * i);
            ConstraintLayout mainLayout = mainScene;
            ImageButton player = new ImageButton(context);
            player.setImageResource(TouchConttroler.getPlayerImageForNumber(i+1, IsMainPlayer));
            int sizePlayer = (int) dMetrics.heightPixels/8; //TouchConttroler.dipToPixels(SIZE_PLAYER_DIP, context);
            ConstraintLayout.LayoutParams imageViewLayoutParams = new ConstraintLayout.LayoutParams(sizePlayer , sizePlayer);
            player.setLayoutParams(imageViewLayoutParams);
            player.setX(positionX);
            player.setY(pY);
            player.setBackgroundColor(0x00FFFFFF);
            player.setScaleType(ImageView.ScaleType.FIT_CENTER);
            player.setOnTouchListener(touch.TouchPlayer);

            mainLayout.addView(player);;
            Settings.playersForIndexWrite.put(player, Index);
            Settings.playersForIndexRead.put(Index, player);

            FrameBuffer.PlayerInformation playerInformation = new FrameBuffer.PlayerInformation();
            playerInformation.Index = Index;
            if(IsMainPlayer) playerInformation.typePlayer = FrameBuffer.TypePlayer.mainPlayer;
            else playerInformation.typePlayer = FrameBuffer.TypePlayer.enemyPlayer;
            playerInformation.Number = i + 1;
            playerInformation.Name = "Name";
            FrameBuffer.playerInformations.add(playerInformation);
            Index++;
        }
    }

    public void SpawnField(ImageView field, ImageView logoField, DisplayMetrics metrics, Context context){
        float locationY = TouchConttroler.dipToPixels(100, context);
        switch (Settings.LoadMainSceneSettings.typeField){
            case full:
                field.setImageResource(R.mipmap.fields_full);
                ViewGroup.LayoutParams paramsFull = field.getLayoutParams();
                paramsFull.height = metrics.heightPixels + (int) TouchConttroler.dipToPixels(150, context);
                paramsFull.width = metrics.widthPixels - (int) TouchConttroler.dipToPixels(140, context);
                field.setLayoutParams(paramsFull);
                return;
            case half:
                field.setImageResource(R.mipmap.fields_1_2);
                ViewGroup.LayoutParams paramsHalf = field.getLayoutParams();
                paramsHalf.height = metrics.heightPixels + (int) TouchConttroler.dipToPixels(120, context);
                paramsHalf.width = metrics.widthPixels - (int) TouchConttroler.dipToPixels(140, context);
                field.setLayoutParams(paramsHalf);
                logoField.setTranslationY(-locationY);
                break;
            case three:
                field.setImageResource(R.mipmap.fields_1_2_3x3);
                ViewGroup.LayoutParams paramsThree = field.getLayoutParams();
                paramsThree.height = metrics.heightPixels + (int) TouchConttroler.dipToPixels(120, context);
                paramsThree.width = metrics.widthPixels - (int) TouchConttroler.dipToPixels(140, context);
                field.setLayoutParams(paramsThree);
                logoField.setTranslationY(-locationY);
                break;
            case four:
                field.setImageResource(R.mipmap.fields_1_2_4x2);
                ViewGroup.LayoutParams paramsFour = field.getLayoutParams();
                paramsFour.height = metrics.heightPixels + (int) TouchConttroler.dipToPixels(120, context);
                paramsFour.width = metrics.widthPixels - (int) TouchConttroler.dipToPixels(140, context);
                field.setLayoutParams(paramsFour);
                logoField.setTranslationY(-locationY);
                break;
            default:
                break;
        }
    }


    public void SpawnNewBall(Context context, ConstraintLayout mainScene, DisplayMetrics dMetrics, TouchConttroler touch, Integer Index) {
        ConstraintLayout mainLayout = mainScene;
        ImageButton player = new ImageButton(context);
        player.setImageResource(R.mipmap.ball);
        int sizeBall = (int) dMetrics.heightPixels/10;
        ConstraintLayout.LayoutParams imageViewLayoutParams = new ConstraintLayout.LayoutParams(sizeBall , sizeBall);
        player.setLayoutParams(imageViewLayoutParams);
        player.setX(dMetrics.widthPixels/2 - sizeBall/2);
        player.setY(dMetrics.heightPixels/2 - sizeBall/2);
        player.setBackgroundColor(0x00FFFFFF);
        player.setScaleType(ImageView.ScaleType.FIT_CENTER);
        player.setOnTouchListener(touch.TouchBall);

        mainLayout.addView(player);
        Settings.ball = player;
        Settings.playersForIndexWrite.put(player, Index);
        Settings.playersForIndexRead.put(Index, player);

        FrameBuffer.PlayerInformation playerInformation = new FrameBuffer.PlayerInformation();
        playerInformation.Index = Index;
        playerInformation.typePlayer = FrameBuffer.TypePlayer.ball;
        playerInformation.Number = 0;
        playerInformation.Name = "Name";
        FrameBuffer.playerInformations.add(playerInformation);
        Index++;
    }


    public void SpawnOldPlayer(Context context, TouchConttroler touch, ConstraintLayout mainLayout, DisplayMetrics dMetrics){
        Settings.playersForIndexWrite = new HashMap();
        Settings.playersForIndexRead = new HashMap();
        for(FrameBuffer.PlayerInformation playerInformation : FrameBuffer.playerInformations){
            int index = playerInformation.Index;
            ImageButton player = new ImageButton(context);
            switch(playerInformation.typePlayer)
            {
                case ball:
                    player.setImageResource(R.mipmap.ball);
                    break;
                case mainPlayer:
                    player.setImageResource(TouchConttroler.getPlayerImageForNumber(playerInformation.Number, true));
                    break;
                case enemyPlayer:
                    player.setImageResource(TouchConttroler.getPlayerImageForNumber(playerInformation.Number, false));
                    break;
            }
            ConstraintLayout.LayoutParams imageViewLayoutParams;
            if(playerInformation.typePlayer == FrameBuffer.TypePlayer.ball) {
                int sizeBall = (int) dMetrics.heightPixels/10;
                imageViewLayoutParams = new ConstraintLayout.LayoutParams(sizeBall, sizeBall);
                player.setOnTouchListener(touch.TouchBall);
                Settings.ball = player;
            } else{
                int sizePlayer = (int) dMetrics.heightPixels/8;
                imageViewLayoutParams = new ConstraintLayout.LayoutParams(sizePlayer, sizePlayer);
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
