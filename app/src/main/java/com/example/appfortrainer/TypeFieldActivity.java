package com.example.appfortrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TypeFieldActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_type_field);
    }

    public void setFullField(View view){
        Settings.LoadMainSceneSettings.typeField = Settings.TypeField.full;
        LoadChangeScene();
    }
    public void setHalfField(View view){
        Settings.LoadMainSceneSettings.typeField = Settings.TypeField.half;
        LoadChangeScene();
    }
    public void setThreeField(View view){
        Settings.LoadMainSceneSettings.typeField = Settings.TypeField.three;
        LoadChangeScene();
    }
    public void setFourField(View view){
        Settings.LoadMainSceneSettings.typeField = Settings.TypeField.four;
        LoadChangeScene();
    }

    private void LoadChangeScene(){
        Intent intent = new Intent(this, PlayersConfigActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
