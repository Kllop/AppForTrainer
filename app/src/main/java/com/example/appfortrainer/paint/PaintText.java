package com.example.appfortrainer.paint;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.appfortrainer.AnimationConttroler;
import com.example.appfortrainer.FrameBuffer;
import com.example.appfortrainer.TouchConttroler;
import com.google.android.material.textfield.TextInputEditText;

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
            float eX = motionEvent.getRawX();
            float eY = motionEvent.getRawY();
            if(MotionEvent.ACTION_DOWN == motionEvent.getAction()){
                    SpawnText(eX, eY, "text");}
            return false;
        }
    };

    public void SpawnText(float x, float y, String text){
        EditText myText = new EditText(context);
        myText.setX(x - (float) TouchConttroler.dipToPixels(90, context));
        myText.setY(y - (float) TouchConttroler.dipToPixels(45, context));
        myText.setText(text);
        myText.setTextSize(50);
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
