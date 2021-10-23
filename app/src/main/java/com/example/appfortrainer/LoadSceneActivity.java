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
        getListView().setBackgroundColor(Color.parseColor("#779EBD"));
        setContentView(R.layout.load_activity);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        FrameBuffer.ResetBuffer();
        Settings.ResetSettings();
        Settings.saveAndLoadComponent.LoadScene(position, this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
    public void BackMainMenu(View view){
        Intent intent = new Intent(this, MainMenuAcitivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

}
