package com.example.appfortrainer;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadSceneActivity extends ListActivity  {

    private int lastTouchIndex = -1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.saveAndLoadComponent.LoadProjectionInfo(this);
        String[] values = new String[]{};
        List<String> names = new ArrayList<String>();
        for (Map.Entry<Integer, String> pair : Settings.saveAndLoadComponent.projectionsInfo.entrySet()) {
            names.add(pair.getValue());
        }
        values = names.toArray(values);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_item_list, R.id.label, values);
        setListAdapter(adapter);
        setContentView(R.layout.load_activity);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        FrameBuffer.ResetBuffer();
        Settings.ResetSettings();
        for(int i=0; i<l.getChildCount(); i++){
            if( i == position){
                l.getChildAt(i).setBackgroundColor(Color.parseColor("#FF018786"));
                lastTouchIndex = position;
            }
            else{l.getChildAt(i).setBackgroundColor(Color.parseColor("#00FFFFFF"));}
        }

    }
    public void OnLoadScene(View view){
        if(lastTouchIndex == -1){return;}
        Settings.isFirstStart = true;
        Settings.saveAndLoadComponent.LoadScene(lastTouchIndex, this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onDestroyScene(View view){
        if(lastTouchIndex == -1){return;}
        Settings.saveAndLoadComponent.DeletedScene(lastTouchIndex, this);
        Restart();
    }
    private void Restart(){
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
    public void BackMainMenu(View view){
        finish();
    }

}
