package we.polo.appfortrainer.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import we.polo.appfortrainer.AnimationConttroler;
import we.polo.appfortrainer.FrameBuffer;
import we.polo.appfortrainer.paint.PaintConttroler.TypePaint;

public class PaintComponent extends View{
    protected Paint paint;
    protected Path path;
    protected Canvas mCanvas;
    protected boolean isComplete = false;
    public boolean isStopPaint = false;
    protected FrameBuffer.PaintUnit paintUnit;
    protected final PaintConttroler paintConttroler;
    private final AnimationConttroler animationConttroler;

    public PaintComponent(Context context, FrameBuffer.PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context);
        init();
        paintUnit = pU;
        paintConttroler = pc;
        animationConttroler = ac;
    }
    public PaintComponent(Context context, AttributeSet attrs, FrameBuffer.PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
        super(context, attrs);
        init();
        paintUnit = pU;
        paintConttroler = pc;
        animationConttroler = ac;
    }

    public PaintComponent(Context context, AttributeSet attrs, int defStyle, FrameBuffer.PaintUnit pU, PaintConttroler pc, AnimationConttroler ac) {
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
        paint = new Paint();//Paint.DITHER_FLAG);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
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
    public void StopTouch(final float X, final float Y){setFocusable(false); setFocusableInTouchMode(false);}

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
            default:
                return false;
        }
        postInvalidate();
        return true;
    }
}
