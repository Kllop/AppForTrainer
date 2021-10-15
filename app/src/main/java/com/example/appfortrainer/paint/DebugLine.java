package com.example.appfortrainer.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.example.appfortrainer.FrameBuffer.Vector2;

public class DebugLine extends View {

    protected Paint paint;
    protected Path path;
    protected Canvas mCanvas;

    public DebugLine(Context context) {
        super(context);
        init();
    }
    public DebugLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DebugLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
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

    public void DebugLineDraw(Vector2 startLocation, Vector2 EndLocation){
        path.reset();
        path.moveTo(startLocation.getX(), startLocation.getY());
        path.lineTo(EndLocation.getX(), EndLocation.getY());
        invalidate();
    }
}
