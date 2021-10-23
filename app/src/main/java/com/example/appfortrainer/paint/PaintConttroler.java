package com.example.appfortrainer.paint;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.appfortrainer.AnimationConttroler;
import com.example.appfortrainer.R;
import com.example.appfortrainer.Settings;
import com.example.appfortrainer.FrameBuffer.PaintUnit;
import com.google.android.material.textfield.TextInputEditText;

public class PaintConttroler{
    public static class Vector2{public float x; public float y;}
    private final Context context;
    private final ConstraintLayout paintScene;
    private PaintComponent currentPaint;
    private AnimationConttroler animationConttroler;

    public enum TypePaint{ BaseLine, DottedLine, Pencil, Text, Eraser }

    public PaintConttroler(Context ctx, ConstraintLayout pScene){
        context = ctx;
        paintScene = pScene;

    }
    public void setAnimationConttroler(AnimationConttroler ac){
        animationConttroler = ac;
    }

    public final void OnPaintLine(){
        if(!Settings.isRecording | Settings.isPlayAnimation | Settings.isPaint){return;}
        Settings.isPaint = true;
        CreateBaseLine();
    }
    public void CreateBaseLine(){
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        PaintUnit paintUnit = new PaintUnit();
        currentPaint = new PaintBaseLine(context, paintUnit, this, animationConttroler);
        paintScene.addView(currentPaint);
        paintUnit.Type = TypePaint.BaseLine;
    }
    public final void OnPaintDottedLine(){
        if(!Settings.isRecording | Settings.isPlayAnimation | Settings.isPaint){return;}
        Settings.isPaint = true;
        CreateDottedLine();
    }
    public void CreateDottedLine(){
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        PaintUnit paintUnit = new PaintUnit();
        currentPaint = new PaintDottedLine(context, paintUnit, this, animationConttroler);
        paintScene.addView(currentPaint);
        paintUnit.Type = TypePaint.DottedLine;
    }
    public final void OnPaintPenсil(){
        if(!Settings.isRecording | Settings.isPlayAnimation | Settings.isPaint){return;}
        Settings.isPaint = true;
        CreatePencil();
    }
    public void CreatePencil(){
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        PaintUnit paintUnit = new PaintUnit();
        currentPaint = new PaintPencilBase(context, paintUnit, this, animationConttroler);
        paintScene.addView(currentPaint);
        paintUnit.Type = TypePaint.Pencil;
    }
    public final void OnPaintText(){
        if(!Settings.isRecording | Settings.isPlayAnimation | Settings.isPaint){return;}
        Settings.isPaint = true;
        CreateText();
    }
    public void CreateText(){
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        PaintUnit paintUnit = new PaintUnit();
        PaintText editText = new PaintText(context, paintUnit, paintScene, animationConttroler, this);
        paintScene.addView(editText);
    }
    public void OnPaintEraser(){
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        ClearPaint();
        PaintUnit paintUnit = new PaintUnit();
        paintUnit.Type = TypePaint.Eraser;
        animationConttroler.AddFrame(paintUnit);
    }
    public void StopPaint(){
        Settings.isPaint = false;
        paintScene.setTranslationZ(2);
        animationConttroler.ResetPlayerPosition();
    }
    public void ViewPaintsInFrame(PaintUnit paintUnit) {
        switch (paintUnit.Type) {
            case Pencil:
                PaintPencilBase pencilLine = new PaintPencilBase(context, paintUnit, this, animationConttroler);
                pencilLine.LoadPaint();
                paintScene.addView(pencilLine);
                break;
            case BaseLine: ;
                PaintBaseLine baseLine = new PaintBaseLine(context, paintUnit, this, animationConttroler);
                baseLine.LoadPaint();
                paintScene.addView(baseLine);
                break;
            case DottedLine:
                PaintDottedLine dottedLine = new PaintDottedLine(context, paintUnit, this, animationConttroler);
                dottedLine.LoadPaint();
                paintScene.addView(dottedLine);
                break;
            case Text:
                PaintText paintText = new PaintText(context, paintUnit, paintScene, animationConttroler, this);
                paintText.LoadPaint();
                paintScene.addView(paintText);
                break;
            case Eraser:
                ClearPaint();
                break;
            default:
                break;
        }
    }

    public void ClearPaint() {
        paintScene.removeAllViews();
        if(currentPaint != null){
            currentPaint.isStopPaint = true;
            currentPaint = null;
        }
    }


}
