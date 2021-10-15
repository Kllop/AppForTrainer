package com.example.appfortrainer;

import android.content.Context;

import com.example.appfortrainer.FrameBuffer.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveAndLoadComponent {
    public class UintSaveProjectionsClass {
        public PlayerPosition playerPosition;
        public List<FrameUnit> frames;
        public String nameFile;
        public int countEnemyPlayer;
        public int countMainPlayer;
        public List<PlayerInformation> playerInformations = new ArrayList<>();
    }
    private class UintSaveSettingsClass {
        public boolean MovePlayerInFirstFrame;
        public boolean FreeAngleAttach;
    }
    public final class Projections {public List<UintSaveProjectionsClass> projects = new ArrayList<UintSaveProjectionsClass>();}
    private final static String FILE_NAME_PROJECTION = "SaveFile.txt";
    private final static String FILE_NAME_SETTINGS = "Settings.txt";

    public Projections projections = new Projections();

    private void SaveProjections(){
        UintSaveProjectionsClass saveClass = new UintSaveProjectionsClass();
        saveClass.countEnemyPlayer = Settings.LoadMainSceneSettings.CountEnemyPlayer;
        saveClass.countMainPlayer = Settings.LoadMainSceneSettings.CountMainPlayer;
        saveClass.nameFile = Settings.LoadMainSceneSettings.NameScene;
        saveClass.frames = FrameBuffer.Frames;
        saveClass.playerPosition = FrameBuffer.PlayerPositionInFrame;
        saveClass.playerInformations = FrameBuffer.playerInformations;
        if(Settings.indexFile == -1){projections.projects.add(saveClass);}
        else{projections.projects.set(Settings.indexFile, saveClass);}
    }
    private void LoadProjection(UintSaveProjectionsClass projection) {
        Settings.LoadMainSceneSettings.CountEnemyPlayer = projection.countEnemyPlayer;
        Settings.LoadMainSceneSettings.CountMainPlayer = projection.countMainPlayer;
        Settings.LoadMainSceneSettings.NameScene = projection.nameFile;
        FrameBuffer.Frames = projection.frames;
        FrameBuffer.PlayerPositionInFrame = projection.playerPosition;
        FrameBuffer.playerInformations = projection.playerInformations;
    }

    private UintSaveSettingsClass SaveSettings(){
        UintSaveSettingsClass saveClass = new UintSaveSettingsClass();
        saveClass.MovePlayerInFirstFrame = Settings.SceneSettings.MovePlayerInFirstFrame;
        saveClass.FreeAngleAttach = Settings.SceneSettings.FreeAngleAttach;
        return saveClass;
    }

    private void LoadSettings(UintSaveSettingsClass saveClass){
        Settings.SceneSettings.MovePlayerInFirstFrame = saveClass.MovePlayerInFirstFrame;
        Settings.SceneSettings.FreeAngleAttach = saveClass.FreeAngleAttach;
    }

    public void SaveInFileProjections(Context context) {
        SaveProjections();
        Gson gson = new Gson();
        String json = gson.toJson(projections);
        StreamController.writeToFile(json, context, FILE_NAME_PROJECTION);
    }

    public void LoadInFileProjections(Context context) {
        Gson gson = new Gson();
        String json = null;
        try {
            json = StreamController.readToFile(context, FILE_NAME_PROJECTION);
            if(json == null){return;}
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Projections proj = gson.fromJson(json, Projections.class);
        if(proj == null){return;}
        else{ projections = gson.fromJson(json, Projections.class);}
    }

    public void SaveInFileSettings(Context context){
        Gson gson = new Gson();
        UintSaveSettingsClass saveClass = SaveSettings();
        String json = gson.toJson(saveClass);
        StreamController.writeToFile(json, context, FILE_NAME_SETTINGS);
    }

    public void LoadInFileSettings(Context context){
        Gson gson = new Gson();
        String json = null;
        try {
            json = StreamController.readToFile(context, FILE_NAME_SETTINGS);
            if(json == null){return;}
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        UintSaveSettingsClass settings = gson.fromJson(json, UintSaveSettingsClass.class);
        if(settings == null){return;}
        LoadSettings(settings);
    }
    public void LoadScene(int Index) {
        Settings.indexFile = Index;
        UintSaveProjectionsClass scene = projections.projects.get(Index);
        LoadProjection(scene);
    }


}
