package we.polo.appfortrainer.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import we.polo.appfortrainer.FrameBuffer;

public class DebugLine extends View {

    protected Paint paint;
    protected Path path;
    protected Canvas mCanvas;
    protected int Color;

    public DebugLine(Context context, int color) {
        super(context);
        Color = color;
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
        paint.setColor(Color);
        paint.setStrokeWidth(5);
        path = new Path();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        mCanvas.drawPath(path, paint);
    }

    public void DebugLineDraw(FrameBuffer.Vector2 startLocation, FrameBuffer.Vector2 EndLocation){
        path.reset();
        path.moveTo(startLocation.getX(), startLocation.getY());
        path.lineTo(EndLocation.getX(), EndLocation.getY());
        invalidate();
    }
}
