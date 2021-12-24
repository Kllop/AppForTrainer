package we.polo.appfortrainer.paint;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;

import we.polo.appfortrainer.AnimationConttroler;
import we.polo.appfortrainer.Settings;

import we.polo.appfortrainer.FrameBuffer;

public class PaintConttroler{
    public static class Vector2{public float x; public float y;}
    private final Context context;
    private final ConstraintLayout paintScene;
    private PaintComponent currentPaint;
    private AnimationConttroler animationConttroler;
    public TypePaint CurrentPaintType;

    public enum TypePaint{ BaseLine, DottedLine, Pencil, Text, Eraser, None }

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
        CurrentPaintType = TypePaint.BaseLine;
        CreateBaseLine();
    }
    public void CreateBaseLine(){
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        FrameBuffer.PaintUnit paintUnit = new FrameBuffer.PaintUnit();
        currentPaint = new PaintBaseLine(context, paintUnit, this, animationConttroler);
        paintScene.addView(currentPaint);
        paintUnit.Type = TypePaint.BaseLine;
    }
    public final void OnPaintDottedLine(){
        if(!Settings.isRecording | Settings.isPlayAnimation | Settings.isPaint){return;}
        Settings.isPaint = true;
        CurrentPaintType = TypePaint.DottedLine;
        CreateDottedLine();
    }
    public void CreateDottedLine(){
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        FrameBuffer.PaintUnit paintUnit = new FrameBuffer.PaintUnit();
        currentPaint = new PaintDottedLine(context, paintUnit, this, animationConttroler);
        paintScene.addView(currentPaint);
        paintUnit.Type = TypePaint.DottedLine;
    }
    public final void OnPaintPenсil(){
        if(!Settings.isRecording | Settings.isPlayAnimation | Settings.isPaint){return;}
        Settings.isPaint = true;
        CurrentPaintType = TypePaint.Pencil;
        CreatePencil();
    }
    public void CreatePencil(){
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        FrameBuffer.PaintUnit paintUnit = new FrameBuffer.PaintUnit();
        currentPaint = new PaintPencilBase(context, paintUnit, this, animationConttroler);
        paintScene.addView(currentPaint);
        paintUnit.Type = TypePaint.Pencil;
    }
    public final void OnPaintText(){
        if(!Settings.isRecording | Settings.isPlayAnimation | Settings.isPaint){return;}
        Settings.isPaint = true;
        CurrentPaintType = TypePaint.Text;
        CreateText();
    }
    public void CreateText(){
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        FrameBuffer.PaintUnit paintUnit = new FrameBuffer.PaintUnit();
        PaintText editText = new PaintText(context, paintUnit, paintScene, animationConttroler, this);
        paintScene.addView(editText);
    }
    public void OnPaintEraser(){
        if(!Settings.isRecording | Settings.isPlayAnimation){return;}
        ClearPaint();
        FrameBuffer.PaintUnit paintUnit = new FrameBuffer.PaintUnit();
        paintUnit.Type = TypePaint.Eraser;
        animationConttroler.AddFrame(paintUnit);
    }
    public void StopPaint(){
        Settings.isPaint = false;
        paintScene.setTranslationZ(2);
        animationConttroler.ResetPlayerPosition();
    }
    public void ViewPaintsInFrame(FrameBuffer.PaintUnit paintUnit) {
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
        CurrentPaintType = TypePaint.None;
        if(currentPaint != null){
            currentPaint.isStopPaint = true;
            currentPaint = null;
        }
    }


}
