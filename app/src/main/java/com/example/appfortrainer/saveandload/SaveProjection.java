package com.example.appfortrainer.saveandload;

import android.content.Context;

import com.example.appfortrainer.FrameBuffer;
import com.example.appfortrainer.Settings;
import com.example.appfortrainer.Settings.TypeField;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveProjection {

    public class UintSaveProjectionsClass {
        public FrameBuffer.PlayerPosition playerPosition;
        public List<FrameBuffer.FrameUnit> frames;
        public String nameFile;
        public int countEnemyPlayer;
        public int countMainPlayer;
        public TypeField typeField;
        public List<FrameBuffer.PlayerInformation> playerInformations = new ArrayList<>();
    }

    private UintSaveProjectionsClass SaveProjections(){
        UintSaveProjectionsClass saveClass = new UintSaveProjectionsClass();
        saveClass.countEnemyPlayer = Settings.LoadMainSceneSettings.CountEnemyPlayer;
        saveClass.countMainPlayer = Settings.LoadMainSceneSettings.CountMainPlayer;
        saveClass.nameFile = Settings.LoadMainSceneSettings.NameScene;
        saveClass.typeField = Settings.LoadMainSceneSettings.typeField;
        saveClass.frames = FrameBuffer.Frames;
        saveClass.playerPosition = FrameBuffer.PlayerPositionInFrame;
        saveClass.playerInformations = FrameBuffer.playerInformations;
        return saveClass;
    }

    private void LoadProjection(UintSaveProjectionsClass projection) {
        Settings.LoadMainSceneSettings.CountEnemyPlayer = projection.countEnemyPlayer;
        Settings.LoadMainSceneSettings.CountMainPlayer = projection.countMainPlayer;
        Settings.LoadMainSceneSettings.NameScene = projection.nameFile;
        Settings.LoadMainSceneSettings.typeField = projection.typeField;
        FrameBuffer.Frames = projection.frames;
        FrameBuffer.PlayerPositionInFrame = projection.playerPosition;
        FrameBuffer.playerInformations = projection.playerInformations;
    }

    public void SaveInFileProjections(Context context, String name) {
        UintSaveProjectionsClass projection = SaveProjections();
        Gson gson = new Gson();
        String json = gson.toJson(projection);
        StreamController.writeToFile(json, context, name);
    }

    public void LoadInFileProjections(Context context, String name) {
        Gson gson = new Gson();
        String json = null;
        try {
            json = StreamController.readToFile(context, name);
            if(json == null){return;}
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        UintSaveProjectionsClass projection = gson.fromJson(json, UintSaveProjectionsClass.class);
        if(projection == null){return;}
        LoadProjection(projection);
    }
    public void DeletedProject(Context context, String name){
        StreamController.deletedFile(context, name);
    }

}
