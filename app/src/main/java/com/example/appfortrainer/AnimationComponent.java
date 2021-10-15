package com.example.appfortrainer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appfortrainer.FrameBuffer.FrameUnit;
import com.example.appfortrainer.FrameBuffer.PaintUnit;
import com.example.appfortrainer.FrameBuffer.MotionPlayer;
import com.example.appfortrainer.paint.PaintConttroler;
import com.hbisoft.hbrecorder.HBRecorder;

import java.lang.ref.SoftReference;
import java.util.List;

public class AnimationComponent extends AppCompatActivity {

    private static final long DEFAULT_ANIMATION_DURATION = 3000L;
    private static final long DEFAULT_ANIMATION_DELAY = 500;
    private ValueAnimator valueAnimatorX;
    private ValueAnimator valueAnimatorY;
    private Ietterator ietterator;
    private List<FrameUnit> frameBuffer;
    private boolean isPlayAnimation = false;
    private HBRecorder hbRecorder;
    private TextView text;
    private final PaintConttroler paintConttroler;
    private final ImageButton imageButton;
    private Handler handler;
    private Runnable runnable;

    public AnimationComponent(TextView mytext, PaintConttroler pConttroler, ImageButton image){
        text = mytext;
        paintConttroler = pConttroler;
        imageButton = image;
    }

    public void OnStartAnimation(SoftReference<Ietterator> itter) {
        if(Settings.isPlayAnimation){return;}
        Settings.isPlayAnimation = true;
        ietterator = itter.get();
        frameBuffer = FrameBuffer.Frames;
        hbRecorder = null;
        Play(false);
    }
    public void OnStartAnimation(SoftReference<Ietterator> itter, HBRecorder Recorder) {
        if(Settings.isPlayAnimation){return;}
        Settings.isPlayAnimation = true;
        ietterator = itter.get();
        int lastValue = ietterator.GetValue();
        ietterator.SetFirstValue();
        frameBuffer = FrameBuffer.Frames;
        hbRecorder = Recorder;
        Play(false);
    }

    private void Play(boolean delay) {
        UpdateText();
        handler = null;
        runnable = null;
        if(ietterator.GetValue() == ietterator.GetMaxValue()){Stop(); return;}
        FrameUnit frame = frameBuffer.get(ietterator.GetValue());
        switch (frame.Type){
            case paint:
                PaintUnit paintUnit = (PaintUnit) frame.frame;
                StartAnimationPaint(paintUnit);
                break;
            case movePlayer:
                MotionPlayer motionPlayer = (MotionPlayer) frame.frame;
                PlayAnimation(motionPlayer, delay);
        }
    }

    public void StartAnimationPaint(PaintUnit paintUnit){
        paintConttroler.ViewPaintsInFrame(paintUnit);
        runnable = new Runnable() {
            @Override
            public void run() {
                UpdateFrame();
                Play(true);
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, Settings.SceneSettings.TimeAnimationImage * 1000);
    }
    private void Stop() {
        UpdateAnimationButton();
        Settings.isPlayAnimation = false;
        valueAnimatorX = null;
        valueAnimatorY = null;
        if(hbRecorder == null){return;}
        hbRecorder.stopScreenRecording();
    }
    public void OnResumeAnimation() {
        valueAnimatorX.resume();
        valueAnimatorY.resume();
    }
    public void OnPauseAnimation() {
        valueAnimatorX.pause();
        valueAnimatorY.pause();
    }

    private void UpdateFrame() {
        ietterator.NextValue();
    }
    private void PlayAnimation(MotionPlayer frame, boolean isDelay) {
        View player = (View) Settings.playersForIndexRead.get(frame.Index);
        valueAnimatorX = ValueAnimator.ofFloat(player.getX(), frame.PositionX);
        valueAnimatorY = ValueAnimator.ofFloat(player.getY(), frame.PositionY);
        boolean WithBoal;
        float LengthBallAttachX = player.getX() - Settings.ball.getX();
        float LengthBallAttachY = player.getY() - Settings.ball.getY();
        if(frame.Index == frame.BallParentIndex){WithBoal = true; Settings.ParentBall = player; }
        else{WithBoal = false; Settings.ParentBall = (View) Settings.playersForIndexRead.get(frame.BallParentIndex);}

        valueAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float valueX = (float) animation.getAnimatedValue();
                player.setX(valueX);
                if(WithBoal){
                    Settings.ball.setX(valueX - LengthBallAttachX);}
            }
        });

        valueAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float valueY = (float) animation.getAnimatedValue();
                player.setY(valueY);
                if(WithBoal){Settings.ball.setY(valueY - LengthBallAttachY);}
            }
        });

        valueAnimatorX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(!Settings.isPlayAnimation){return;}
                super.onAnimationEnd(animation);
                UpdateFrame();
                Play(true);
            }
        });

        valueAnimatorX.setInterpolator(new LinearInterpolator());
        valueAnimatorY.setInterpolator(new LinearInterpolator());

        valueAnimatorX.setDuration(DEFAULT_ANIMATION_DURATION);
        valueAnimatorY.setDuration(DEFAULT_ANIMATION_DURATION);

        if(isDelay){
            valueAnimatorX.setStartDelay(DEFAULT_ANIMATION_DELAY);
            valueAnimatorY.setStartDelay(DEFAULT_ANIMATION_DELAY);
        }
        valueAnimatorX.start();
        valueAnimatorY.start();
    }

    public void OnStopAnimation() {
        Settings.isPlayAnimation = false;
        if(valueAnimatorX != null){valueAnimatorX.cancel(); valueAnimatorX = null;}
        if(valueAnimatorY != null){valueAnimatorY.cancel(); valueAnimatorY = null;}
        if(handler != null){
            handler.removeCallbacks(runnable);
        }
    }
    private void UpdateText() {
        text.setText(String.valueOf(AnimationConttroler.ietterator.GetValue() + 1)
                + "/" + String.valueOf(FrameBuffer.Frames.size() + 1));
    }
    private void UpdateAnimationButton() {
        imageButton.setImageResource(R.drawable.round_blue_play);
    }

}
