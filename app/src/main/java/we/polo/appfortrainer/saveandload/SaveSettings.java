package we.polo.appfortrainer.saveandload;
import android.content.Context;

import we.polo.appfortrainer.Settings;

import com.google.gson.Gson;

import java.io.IOException;

public class SaveSettings {
    private final String FILE_NAME_SETTINGS ;

    public SaveSettings(String name){
        FILE_NAME_SETTINGS = name;
    }

    public class UintSaveSettingsClass {
        public boolean MovePlayerInFirstFrame;
        public boolean FreeAngleAttach;
        public boolean Vibration;
        public float AnimationSpeed;
    }

    private void LoadSettings(UintSaveSettingsClass saveClass){
        Settings.SceneSettings.MovePlayerInFirstFrame = saveClass.MovePlayerInFirstFrame;
        Settings.SceneSettings.FreeAngleAttach = saveClass.FreeAngleAttach;
        Settings.SceneSettings.Vibration = saveClass.Vibration;
        Settings.SceneSettings.AnimationSpeed = saveClass.AnimationSpeed;
    }

    private UintSaveSettingsClass SaveSettings(){
        UintSaveSettingsClass saveClass = new UintSaveSettingsClass();
        saveClass.MovePlayerInFirstFrame = Settings.SceneSettings.MovePlayerInFirstFrame;
        saveClass.FreeAngleAttach = Settings.SceneSettings.FreeAngleAttach;
        saveClass.Vibration = Settings.SceneSettings.Vibration;
        saveClass.AnimationSpeed = Settings.SceneSettings.AnimationSpeed;
        return saveClass;
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
}
