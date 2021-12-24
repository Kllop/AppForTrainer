package we.polo.appfortrainer.paint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import we.polo.appfortrainer.AnimationConttroler;
import we.polo.appfortrainer.FrameBuffer;
import we.polo.appfortrainer.R;
import we.polo.appfortrainer.TouchConttroler;

public class PaintText extends ConstraintLayout {

    private FrameBuffer.PaintUnit paintUnit;
    private ConstraintLayout paintScene;
    private Context context;
    private AnimationConttroler animationConttroler;
    private boolean isStopPaint = false;
    private PaintConttroler paintConttroler;

    public PaintText(Context context, FrameBuffer.PaintUnit pU, ConstraintLayout paintS, AnimationConttroler animConttroler, PaintConttroler pConttroler) {
        super(context);
        this.setOnTouchListener(touchListener);
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setLayoutParams(linLayoutParam);
        this.context = context;
        paintUnit = pU;
        paintScene = paintS;
        animationConttroler = animConttroler;
        paintConttroler = pConttroler;
    }

    OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(isStopPaint){return false;}
            float eX = motionEvent.getRawX() - (float) TouchConttroler.dipToPixels(60, context);
            float eY = motionEvent.getRawY() - (float) TouchConttroler.dipToPixels(30, context);
            if(MotionEvent.ACTION_DOWN == motionEvent.getAction()){
                    SpawnText(eX, eY, "text");}
            return false;
        }
    };

    public void SpawnText(float x, float y, String text){
        EditText myText = new EditText(context);
        myText.setX(x);
        myText.setY(y);
        myText.setText(text);
        myText.setTextSize(30);
        int color = Color.parseColor("#00FFFFFF");
        myText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        paintScene.addView(myText);
        if(isStopPaint){myText.setInputType(InputType.TYPE_NULL);}
        else{
            myText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                    if (isStopPaint) {
                        return false;
                    }
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (event == null || !event.isShiftPressed()) {
                            textView.setFocusable(false);
                            SavePaintUnit(textView);
                            isStopPaint = true;
                            textView.setInputType(InputType.TYPE_NULL);
                            paintConttroler.CreateText();
                            return false;
                        }
                    }
                    return false;
                }
            });
        }
    };

    private void SavePaintUnit(TextView textView){
        paintUnit.Type = PaintConttroler.TypePaint.Text;
        PaintConttroler.Vector2 position = new PaintConttroler.Vector2();
        position.x = textView.getX();
        position.y = textView.getY();
        paintUnit.ListPoints.clear();
        paintUnit.ListPoints.add(position);
        paintUnit.text = String.valueOf(textView.getText());
        animationConttroler.AddFrame(paintUnit);
    }


    public void LoadPaint(){
        isStopPaint = true;
        PaintConttroler.Vector2 position = paintUnit.ListPoints.get(0);
        SpawnText(position.x, position.y, paintUnit.text);
    }

}
