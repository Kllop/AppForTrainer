package we.polo.appfortrainer.paint;

import android.content.Context;
import android.graphics.DashPathEffect;
import android.util.AttributeSet;

import we.polo.appfortrainer.AnimationConttroler;
import we.polo.appfortrainer.FrameBuffer;

public class PaintDottedLine extends PaintBaseLine{

    public PaintDottedLine(Context context, FrameBuffer.PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, pU, pc, ac);
    }
    public PaintDottedLine(Context context, AttributeSet attrs, FrameBuffer.PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, attrs, pU, pc, ac);
    }
    public PaintDottedLine(Context context, AttributeSet attrs, int defStyle, FrameBuffer.PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, attrs, defStyle, pU, pc, ac);
    }
    @Override
    protected void init(){
        super.init();
        paint.setPathEffect(new DashPathEffect(new float[] {20f,30f}, 0f));
    }
}
