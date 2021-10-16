package com.example.appfortrainer;

import android.view.View;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Settings {
    public enum TypeField{ full, half, three, four }
    public final static class LoadMainSceneSettings{
        public static String NameScene;
        public static int CountMainPlayer;
        public static int CountEnemyPlayer;
        public static TypeField typeField = TypeField.full;
    }
    public final static class SceneSettings implements Serializable {
        public static boolean MovePlayerInFirstFrame = true;
        public static boolean FreeAngleAttach = true;
        public static int TimeAnimationImage = 2;
    }
    public static Map<View, Integer> playersForIndexWrite = new HashMap();
    public static Map<Integer, View> playersForIndexRead = new HashMap();
    public static final SaveAndLoadComponent saveAndLoadComponent = new SaveAndLoadComponent();

    public static View ball;
    public static boolean isRecording;
    public static boolean isPlayAnimation;
    public static boolean isPaint;
    public static int indexFile = -1;
    public static View ParentBall;

    public static void ResetSettings(){
        ball = null;
        ParentBall = null;
        isPaint = false;
        isPlayAnimation = false;
        isRecording = false;
    }
}
