package com.example.appfortrainer.paint;

import android.content.Context;
import android.util.AttributeSet;

import com.example.appfortrainer.AnimationConttroler;
import com.example.appfortrainer.TouchConttroler;
import com.example.appfortrainer.paint.PaintConttroler.Vector2;
import com.example.appfortrainer.FrameBuffer.PaintUnit;

public class PaintPencilBase extends PaintComponent{
    private Context context;

    public PaintPencilBase(Context context, PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, pU, pc, ac);
        this.context = context;
    }
    public PaintPencilBase(Context context, AttributeSet attrs, PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, attrs, pU, pc, ac);
    }

    public PaintPencilBase(Context context, AttributeSet attrs, int defStyle, PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, attrs, defStyle, pU, pc, ac);
    }

    @Override
    public void startTouch(final float X, final float Y){
        path.reset();
        path.moveTo(X, Y);
        Vector2 Location = new Vector2();
        Location.x = X;
        Location.y = Y;
        paintUnit.ListPoints.add(Location);
    }

    @Override
    public void MoveTouch(final float X, final float Y){
        Vector2 lastLocation = paintUnit.ListPoints.get(paintUnit.ListPoints.size() - 1);
        Vector2 Location = new Vector2();
        Location.x = X;
        Location.y = Y;
        if(TouchConttroler.LengthVector(lastLocation.x - Location.x, lastLocation.y - Location.y) > TouchConttroler.dipToPixels(10, context)){
            path.lineTo(X, Y);
            paintUnit.ListPoints.add(Location);
        }
    }

    @Override
    public void StopTouch(final float X, final float Y){
        super.StopTouch(X, Y);
        SavePaint();
    }
}
