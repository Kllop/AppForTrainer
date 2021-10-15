package com.example.appfortrainer.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.appfortrainer.AnimationConttroler;
import com.example.appfortrainer.FrameBuffer.PaintUnit;

public class PaintComponent extends View{
    protected Paint paint;
    protected Path path;
    protected Canvas mCanvas;
    protected boolean isComplete = false;
    public boolean isStopPaint = false;
    protected PaintUnit paintUnit;
    protected final PaintConttroler paintConttroler;
    private final AnimationConttroler animationConttroler;

    public PaintComponent(Context context, PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context);
        init();
        paintUnit = pU;
        paintConttroler = pc;
        animationConttroler = ac;
    }
    public PaintComponent(Context context, AttributeSet attrs, PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, attrs);
        init();
        paintUnit = pU;
        paintConttroler = pc;
        animationConttroler = ac;
    }

    public PaintComponent(Context context, AttributeSet attrs, int defStyle, PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, attrs, defStyle);
        init();
        paintUnit = pU;
        paintConttroler = pc;
        animationConttroler = ac;
    }

    public void LoadPaint(){
        isStopPaint = true;
        boolean firstValue = true;
        for(PaintConttroler.Vector2 location : paintUnit.ListPoints){
            if(firstValue){path.moveTo(location.x, location.y); firstValue = false; continue;}
            path.lineTo(location.x, location.y);
        }
        invalidate();
    }

    protected void SavePaint(){
        animationConttroler.AddFrame(paintUnit);
    }

    protected void onNewPaint(){
        isStopPaint = true;
        switch (paintUnit.Type){
            case DottedLine:
                paintConttroler.CreateDottedLine();
                break;
            case Pencil:
                paintConttroler.CreatePencil();
                break;
            case BaseLine:
                paintConttroler.CreateBaseLine();
                break;
        }
    }

    protected void init() {
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        path = new Path();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        mCanvas.drawPath(path, paint);
    }

    public void startTouch(final float X, final float Y){}
    public void MoveTouch(final float X, final float Y){}
    public void StopTouch(final float X, final float Y){}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isStopPaint){return false;}
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                MoveTouch(x, y);
                break;
            case MotionEvent.ACTION_UP:
                StopTouch(x, y);
                onNewPaint();
                break;
        }
        return true;
    }
}
