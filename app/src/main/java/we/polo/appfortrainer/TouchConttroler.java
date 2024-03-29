package we.polo.appfortrainer;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.math.MathUtils;

import java.util.Map;

public class TouchConttroler {

    public AnimationConttroler animationConttroler;
    private final DoubleTap doubleTap = new DoubleTap();
    private static float LENGTH_ATTACH_BALL;
    private static float LENGTH_DEATTACH_BALL;
    private final float SIZE_PLAYER_PX;
    private final float SIZE_BALL_PX;
    VibrationComponent vibration;
    private float evX, evY;
    private final TextView text;
    private final ConstraintLayout numerPlayerMenu;
    private int MaxWidthField;
    private int MaxHeightField;
    private int MinWidthField;
    private int MinHeightField;
    public ImageButton currentPlayerNumber;

    public TouchConttroler(VibrationComponent vib, TextView mytext, AnimationConttroler animConttroler, ConstraintLayout number_menu, DisplayMetrics dMetrics) {
        vibration = vib;
        int square = dMetrics.widthPixels * dMetrics.heightPixels;
        SIZE_PLAYER_PX = (int) Math.sqrt(square/160);
        SIZE_BALL_PX = (int) Math.sqrt(square/235);
        LENGTH_DEATTACH_BALL = SIZE_PLAYER_PX;
        LENGTH_ATTACH_BALL = SIZE_PLAYER_PX/2f;
        text = mytext;
        animationConttroler = animConttroler;
        numerPlayerMenu = number_menu;
    }

    public void setFieldInformation(ImageView field, DisplayMetrics dMetrics){
        ViewGroup.LayoutParams paramsFull = field.getLayoutParams();
        MaxWidthField = (dMetrics.widthPixels/2) + (paramsFull.width/2);
        MinWidthField = (dMetrics.widthPixels/2) - (paramsFull.width/2);
        MaxHeightField = dMetrics.heightPixels;
        MinHeightField = 0;
    }

    float LengthBallAttachX;
    float LengthBallAttachY;

    public static int getPlayerImageForNumber(int number, boolean isMain){
        switch (number){
            case 1:
                if(isMain){return R.mipmap.white_player_1;}
                else{return R.mipmap.blue_player_1;}
            case 2:
                if(isMain){return R.mipmap.white_player_2;}
                else{return R.mipmap.blue_player_2;}
            case 3:
                if(isMain){return R.mipmap.white_player_3;}
                else{return R.mipmap.blue_player_3;}
            case 4:
                if(isMain){return R.mipmap.white_player_4;}
                else{return R.mipmap.blue_player_4;}
            case 5:
                if(isMain){return R.mipmap.white_player_5;}
                else{return R.mipmap.blue_player_5;}
            case 6:
                if(isMain){return R.mipmap.white_player_6;}
                else{return R.mipmap.blue_player_6;}
            case 7:
                if(isMain){return R.mipmap.white_player_7;}
                else{return R.mipmap.blue_player_7;}
            case 8:
                if(isMain){return R.mipmap.white_player_8;}
                else{return R.mipmap.blue_player_8;}
            case 9:
                if(isMain){return R.mipmap.white_player_9;}
                else{return R.mipmap.blue_player_9;}
            case 10:
                if(isMain){return R.mipmap.white_player_10;}
                else{return R.mipmap.blue_player_10;}
            case 11:
                if(isMain){return R.mipmap.white_player_11;}
                else{return R.mipmap.blue_player_11;}
            case 12:
                if(isMain){return R.mipmap.white_player_12;}
                else{return R.mipmap.blue_player_12;}
            case 13:
                if(isMain){return R.mipmap.white_player_13;}
                else{return R.mipmap.blue_player_13;}
        }
        return -1;
    }

    public View.OnTouchListener TouchPlayer = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if(Settings.isPlayAnimation | Settings.isPaint){return false;}
            if(!Settings.isRecording){
                if(MotionEvent.ACTION_DOWN == event.getAction() & doubleTap.Touch(view)) {
                    numerPlayerMenu.setVisibility(View.VISIBLE);
                    currentPlayerNumber = (ImageButton) view;
                    return true;
                }}
            if(!(FrameBuffer.PlayerPositionInFrame == null & Settings.SceneSettings.MovePlayerInFirstFrame) & !Settings.isRecording){return false;}
            float temp = SIZE_PLAYER_PX/2;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(Settings.isRecording){vibration.Vibration();}
                    evX = (view.getX()) - event.getRawX();
                    evY = (view.getY()) - event.getRawY();
                    LengthBallAttachX = view.getX() - Settings.ball.getX();
                    LengthBallAttachY = view.getY() - Settings.ball.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float X = MathUtils.clamp(evX + event.getRawX(), MinWidthField, MaxWidthField - SIZE_PLAYER_PX);
                    float Y = MathUtils.clamp(evY + event.getRawY(), MinHeightField, MaxHeightField - SIZE_PLAYER_PX);
                    view.setX(X);
                    view.setY(Y);
                    if(Settings.ParentBall == view){
                        Settings.ball.setX(view.getX() - LengthBallAttachX);
                        Settings.ball.setY(view.getY() - LengthBallAttachY);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(!Settings.isRecording){break;}
                    animationConttroler.AddFrame(view);
                    UpdateFrameCounter();
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    View.OnTouchListener TouchBall = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if(Settings.isPlayAnimation | Settings.isPaint){return false;}
            if(!(FrameBuffer.Frames.size() == 0 & Settings.SceneSettings.MovePlayerInFirstFrame) & !Settings.isRecording){return false;}
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(Settings.isRecording){vibration.Vibration();}
                    evX = view.getX() - event.getRawX();
                    evY = view.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(Settings.ParentBall != null){LengthBallInPlayer(event.getRawX(), event.getRawY()); return true;}
                    float X = MathUtils.clamp(evX + event.getRawX(), MinWidthField, MaxWidthField - SIZE_BALL_PX);
                    float Y = MathUtils.clamp(evY + event.getRawY(), MinHeightField, MaxHeightField - SIZE_BALL_PX);
                    view.setX(X);
                    view.setY(Y);
                    if(Settings.SceneSettings.FreeAngleAttach) {
                        MoveBall(view, view.getX() + (SIZE_BALL_PX / 2), view.getY() + (SIZE_BALL_PX / 2));
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(!Settings.isRecording){break;}
                    animationConttroler.AddFrame(view);
                    UpdateFrameCounter();
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private void UpdateFrameCounter(){
        text.setText(String.valueOf(AnimationConttroler.ietterator.GetValue() + 1)
                + "/" + String.valueOf(FrameBuffer.Frames.size() + 1));
    }

    private void MoveBall(View ball, float eX, float eY){
        for(Map.Entry<Integer, View> pair : Settings.playersForIndexRead.entrySet()) {
            if(ball == pair.getValue()){continue;}
            View player = (View) pair.getValue();
            final float LengthX = player.getX() + (SIZE_PLAYER_PX/2) - eX;
            final float LengthY = player.getY()+ (SIZE_PLAYER_PX/2) - eY;
            double LengthVector = LengthVector(LengthX, LengthY);
            if(LengthVector < SIZE_PLAYER_PX/2 & player != Settings.ParentBall) {
                double VectorX, VectorY;
                FrameBuffer.Vector2 myVector = NormalizeVector2(LengthX, LengthY);
                VectorX = myVector.getX() * LENGTH_ATTACH_BALL;
                VectorY = myVector.getY() * LENGTH_ATTACH_BALL;
                ball.setX(player.getX() + (SIZE_PLAYER_PX/2) - (SIZE_BALL_PX/2) - (float) VectorX);
                ball.setY(player.getY() + (SIZE_PLAYER_PX/2) - (SIZE_BALL_PX/2) - (float) VectorY);
                Settings.ParentBall = player;
                vibration.Vibration();
                return;
            }
        }
    }

    private class DoubleTap{
        private View lastParentTouch;
        private final long DOUBLE_TOUCH_TIME_DELTA = 100;
        private final long MAX_TIME_DOUBLE_TOUCH = 600;
        private long lastTouchTime;

        public boolean Touch(View parent){
            long deltaTouch = System.currentTimeMillis() - lastTouchTime;
            if(deltaTouch > MAX_TIME_DOUBLE_TOUCH){lastParentTouch = null;}
            if(lastParentTouch == null | lastParentTouch != parent){lastTouchTime = System.currentTimeMillis(); lastParentTouch = parent; return false;}
            else if(DOUBLE_TOUCH_TIME_DELTA < deltaTouch){lastParentTouch = null; return true;}
            else{return false;}
        }
    }

    private void LengthBallInPlayer(float eX, float eY) {
        final float LengthX = Settings.ParentBall.getX() + (SIZE_PLAYER_PX/2) - eX;
        final float LengthY = Settings.ParentBall.getY() + (SIZE_PLAYER_PX/2) - eY;
        if(LengthVector(LengthX, LengthY) > LENGTH_DEATTACH_BALL){
            Settings.ParentBall = null;
        }
    }
    public static double LengthVector(final float x , final float y) {
        return Math.sqrt((x * x) + (y * y));
    }
    private FrameBuffer.Vector2 NormalizeVector2(final float eX , final float eY) {
        float MaxValue = Math.max(Math.abs(eX), Math.abs(eY));
        return new FrameBuffer.Vector2(eX/MaxValue, eY/MaxValue);
    }

    public static float dipToPixels(float dipValue, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

}
