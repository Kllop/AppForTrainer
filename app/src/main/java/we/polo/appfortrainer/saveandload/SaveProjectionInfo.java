package we.polo.appfortrainer.saveandload;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveProjectionInfo {
    private final String FILE_NAME_PROJECTION;

    private class ListUnitSaveProjectionsInfo{
        List<String> projectionsInfos = new ArrayList<>();
    }

    public SaveProjectionInfo(String name){
        FILE_NAME_PROJECTION = name;
    }

    private ListUnitSaveProjectionsInfo ProjectionsInfoToSaveUnit(HashMap<Integer, String> info){
        ListUnitSaveProjectionsInfo listProjectionsInfo = new ListUnitSaveProjectionsInfo();
        for(Map.Entry<Integer, String> pair : info.entrySet()){
            listProjectionsInfo.projectionsInfos.add(pair.getValue());
        }
        return listProjectionsInfo;
    }

    public void SaveProjectionsInfo(Context context, HashMap<Integer, String> info){
        ListUnitSaveProjectionsInfo projectionsInfos = ProjectionsInfoToSaveUnit(info);
        Gson gson = new Gson();
        String json = gson.toJson(projectionsInfos);
        StreamController.writeToFile(json, context, FILE_NAME_PROJECTION);
    }

    private void LoadProjectionsInfo(ListUnitSaveProjectionsInfo projections, HashMap<Integer, String> info){
        int i = 0;
        info.clear();
        for(String name : projections.projectionsInfos){
            info.put(i , name);
            i++;
        }
    }

    public void LoadProjectionsInfo(Context context, HashMap<Integer, String> info){
        Gson gson = new Gson();
        String json = null;
        try {
            json = StreamController.readToFile(context, FILE_NAME_PROJECTION);
            if(json == null){return;}
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ListUnitSaveProjectionsInfo projections = gson.fromJson(json, ListUnitSaveProjectionsInfo.class);
        if(projections == null){return;}
        LoadProjectionsInfo(projections,info);
    }
}
