package com.example.appfortrainer.paint;

import android.content.Context;
import android.graphics.DashPathEffect;
import android.util.AttributeSet;

import com.example.appfortrainer.AnimationConttroler;
import com.example.appfortrainer.FrameBuffer.PaintUnit;

public class PaintDottedLine extends PaintBaseLine{

    public PaintDottedLine(Context context, PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, pU, pc, ac);
    }
    public PaintDottedLine(Context context, AttributeSet attrs, PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, attrs, pU, pc, ac);
    }
    public PaintDottedLine(Context context, AttributeSet attrs, int defStyle, PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, attrs, defStyle, pU, pc, ac);
    }
    @Override
    protected void init(){
        super.init();
        paint.setPathEffect(new DashPathEffect(new float[] {20f,30f}, 0f));
    }
}
