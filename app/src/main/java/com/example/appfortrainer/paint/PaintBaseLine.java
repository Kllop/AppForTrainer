package com.example.appfortrainer.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import com.example.appfortrainer.AnimationConttroler;
import com.example.appfortrainer.paint.PaintConttroler.Vector2;
import com.example.appfortrainer.FrameBuffer.PaintUnit;

public class PaintBaseLine extends PaintComponent{
    private Vector2 StartLocation;
    protected Path pathTip;
    protected Paint paintTip;
    protected final int LENTH_TIP = 35;

    public PaintBaseLine(Context context, PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, pU, pc, ac);
        initTip();
    }
    public PaintBaseLine(Context context, AttributeSet attrs, PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, attrs, pU, pc, ac);
        initTip();
    }
    public PaintBaseLine(Context context, AttributeSet attrs, int defStyle, PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, attrs, defStyle, pU, pc, ac);
        initTip();
    }

    private void initTip(){
        pathTip = new Path();
        paintTip = new Paint(Paint.DITHER_FLAG);
        paintTip.setStyle(Paint.Style.STROKE);
        paintTip.setAntiAlias(true);
        paintTip.setColor(Color.BLACK);
        paintTip.setStrokeWidth(5);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas.drawPath(pathTip, paintTip);
    }

    @Override
    public void startTouch(final float X, final float Y){
        path.moveTo(X, Y);
        Vector2 location = new Vector2();
        location.x = X;
        location.y = Y;
        paintUnit.ListPoints.add(location);
        StartLocation = location;
    }

    @Override
    public void MoveTouch(final float X, final float Y){
        path.reset();
        path.moveTo(StartLocation.x, StartLocation.y);
        path.lineTo(X, Y);
        pathTip.reset();
        Vector2 normalizeVector = NormalizeVector2(StartLocation.x, StartLocation.y, X, Y);
        Vector2 vector1 = AngleVector2(normalizeVector, 60);
        Vector2 vector2 = AngleVector2(normalizeVector, -60);
        pathTip.moveTo(X + vector1.x * LENTH_TIP, Y + vector1.y * LENTH_TIP);
        pathTip.lineTo(X, Y);
        pathTip.lineTo(X + vector2.x * LENTH_TIP, Y + vector2.y * LENTH_TIP);
        invalidate();
    }

    @Override
    public void LoadPaint(){
        isStopPaint = true;
        StartLocation = paintUnit.ListPoints.get(0);
        Vector2 EndLocation = paintUnit.ListPoints.get(1);
        pathTip.reset();
        Vector2 normalizeVector = NormalizeVector2(StartLocation.x, StartLocation.y, EndLocation.x, EndLocation.y);
        Vector2 vector1 = AngleVector2(normalizeVector, 60);
        Vector2 vector2 = AngleVector2(normalizeVector, -60);
        path.moveTo(StartLocation.x, StartLocation.y);
        path.lineTo(EndLocation.x, EndLocation.y);
        pathTip.moveTo(EndLocation.x + vector1.x * LENTH_TIP, EndLocation.y + vector1.y * LENTH_TIP);
        pathTip.lineTo(EndLocation.x, EndLocation.y);
        pathTip.lineTo(EndLocation.x + vector2.x * LENTH_TIP, EndLocation.y + vector2.y * LENTH_TIP);
        invalidate();
    }


    @Override
    public void StopTouch(final float X, final float Y){
        super.StopTouch(X, Y);
        Vector2 location = new Vector2();
        location.x = X;
        location.y = Y;
        paintUnit.ListPoints.add(location);
        SavePaint();
    }

    public Vector2 NormalizeVector2(float x, float y, float X, float Y){
        float maxValue = Math.max(Math.abs(x-X),Math.abs(y-Y));
        Vector2 vector = new Vector2();
        vector.y = (Y-y)/maxValue;
        vector.x = (X-x)/maxValue;
        return vector;
    }

    public Vector2 AngleVector2(Vector2 vector2, float angle){
        Vector2 newVector = new Vector2();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        newVector.x = vector2.x * cos - vector2.y * sin;
        newVector.y = vector2.x * sin + vector2.y * cos;
        return newVector;
    }

}
