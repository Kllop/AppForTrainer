package com.example.appfortrainer;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class VibrationComponent {

    private Context scene;
    private static final int VIB_TIME = 150;

    public VibrationComponent(Context context){
        scene = context;
    }
    public void Vibration(){
        Vibrator vibrator = (Vibrator) scene.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(VIB_TIME, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(VIB_TIME);
        }
    }
}
