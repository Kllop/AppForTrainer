package we.polo.appfortrainer.saveandload;

import android.content.Context;

import we.polo.appfortrainer.Settings;

import java.util.HashMap;
import java.util.Map;

public class SaveAndLoadComponent {

    private final static String FILE_NAME_PROJECTION = "SaveFile.txt";
    private final static String FILE_NAME_SETTINGS = "Settings.txt";
    private final SaveSettings saveSettings = new SaveSettings(FILE_NAME_SETTINGS);
    private final SaveProjection saveProjection = new SaveProjection();
    private final SaveProjectionInfo saveProjectionInfo = new SaveProjectionInfo(FILE_NAME_PROJECTION);

    public HashMap<Integer, String> projectionsInfo = new HashMap<>();

    public void SaveScene(Context context, String name){
        String NameFile = name;
        saveProjection.SaveInFileProjections(context, NameFile);
        if(Settings.indexFile == -1){int index = FindLastIndex();
            projectionsInfo.put(index, NameFile);
            Settings.indexFile = index;
        } else{projectionsInfo.put(Settings.indexFile, NameFile);}
        SaveProjectionInfo(context);
    }

    public void LoadScene(int Index, Context context) {
        Settings.indexFile = Index;
        String name = projectionsInfo.get(Index);
        saveProjection.LoadInFileProjections(context , name);
    }

    public void SaveSettings(Context context){
        saveSettings.SaveInFileSettings(context);
    }

    public void LoadSettings(Context context){
        saveSettings.LoadInFileSettings(context);
    }

    private void SaveProjectionInfo(Context context){
        saveProjectionInfo.SaveProjectionsInfo(context, projectionsInfo);
    }

    public void LoadProjectionInfo(Context context){
        saveProjectionInfo.LoadProjectionsInfo(context, projectionsInfo);
    }

    private int FindLastIndex(){
        int maxValue = 0;
        for(Map.Entry<Integer, String> hash : projectionsInfo.entrySet()){
            if(maxValue < hash.getKey()){maxValue = hash.getKey();}
        }
        return maxValue+1;
    }

    public void DeletedScene(int index, Context context){
        String name = projectionsInfo.get(index);
        saveProjection.DeletedProject(context, name);
        projectionsInfo.remove(index);
        SaveProjectionInfo(context);
    }

}
