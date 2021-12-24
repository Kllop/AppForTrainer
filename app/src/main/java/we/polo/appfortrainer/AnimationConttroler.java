package we.polo.appfortrainer;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import we.polo.appfortrainer.paint.PaintConttroler;
import com.hbisoft.hbrecorder.HBRecorder;

import java.lang.ref.SoftReference;

public class AnimationConttroler
{
    public static final Ietterator ietterator = new Ietterator();
    private final AnimationComponent animationComponent;
    private final TextView text;
    private final PaintConttroler paintConttroler;

    public  AnimationConttroler(TextView mytext, ImageButton image, PaintConttroler pConttroler){
        text = mytext;
        animationComponent = new AnimationComponent(text, pConttroler, image);
        paintConttroler = pConttroler;
    }

    private FrameBuffer.MotionPlayer CreateFrame(View player) {
        FrameBuffer.MotionPlayer motionPlayer = new FrameBuffer.MotionPlayer();
        motionPlayer.PositionX = player.getX();
        motionPlayer.PositionY = player.getY();
        motionPlayer.Index = (int) Settings.playersForIndexWrite.get(player);
        if(Settings.ParentBall == null){
            motionPlayer.BallParentIndex = -1;
        }else{
            motionPlayer.BallParentIndex = (int) Settings.playersForIndexWrite.get(Settings.ParentBall);
        }
        return motionPlayer;
    }
    private void AddNewFrame(FrameBuffer.FrameUnit frame) {
        FrameBuffer.Frames.add(frame);
        ietterator.SetMaxValue(FrameBuffer.Frames.size());
        ietterator.NextValue();
    }
    private void RemoveChangeFrame(FrameBuffer.FrameUnit frame) {
        FrameBuffer.Frames.set(ietterator.GetValue(), frame);
        ietterator.NextValue();
    }

    public void SaveAllPlayerPosition() {
        FrameBuffer.PlayerPosition playersPosition = new FrameBuffer.PlayerPosition();
        for(Object key : Settings.playersForIndexRead.keySet())
        {
            FrameBuffer.MotionPlayer playerPosition = new FrameBuffer.MotionPlayer();
            View player = (View) Settings.playersForIndexRead.get(key);
            playerPosition.PositionX = player.getX();
            playerPosition.PositionY = player.getY();
            playerPosition.Index = (int) Settings.playersForIndexWrite.get(player);
            playersPosition.playerPosition.add(playerPosition);
        }
        if(Settings.ParentBall == null){ playersPosition.BallParentIndex = -1;}
        else{playersPosition.BallParentIndex = (int) Settings.playersForIndexWrite.get(Settings.ParentBall);}
        FrameBuffer.PlayerPositionInFrame = playersPosition;
    }

    private void DeleteForwardFrame(int index) {
        for(int i = ietterator.GetMaxValue(); i > index; i--){
            FrameBuffer.Frames.remove(i-1);
        }
        UpdateFrameCounter();
        ietterator.SetMaxValue(index);
    }

    public void AddFrame(View player) {
        FrameBuffer.FrameUnit frameUnit = new FrameBuffer.FrameUnit();
        frameUnit.Type = FrameBuffer.TypeFrame.movePlayer;
        frameUnit.frame = CreateFrame(player);
        if(ietterator.GetValue() == ietterator.GetMaxValue()){
            AddNewFrame(frameUnit);
        }
        else{
            RemoveChangeFrame(frameUnit);
            DeleteForwardFrame(ietterator.GetValue());
        }
        UpdateFrameCounter();
    }

    public void AddFrame(FrameBuffer.PaintUnit paintUnit){
        FrameBuffer.FrameUnit frameUnit = new FrameBuffer.FrameUnit();
        frameUnit.Type = FrameBuffer.TypeFrame.paint;
        frameUnit.paint = paintUnit;
        if(ietterator.GetValue() == ietterator.GetMaxValue()){
            AddNewFrame(frameUnit);
        }
        else{
            RemoveChangeFrame(frameUnit);
            DeleteForwardFrame(ietterator.GetValue());
        }
        UpdateFrameCounter();
    }

    private void UpdateFrameCounter()
    {
        text.setText(String.valueOf(AnimationConttroler.ietterator.GetValue() + 1)
                + "/" + String.valueOf(FrameBuffer.Frames.size() + 1));
    }


    private void RemovePlayerPosition(){
        paintConttroler.ClearPaint();
        for(FrameBuffer.MotionPlayer player : FrameBuffer.PlayerPositionInFrame.playerPosition){
            View view = (View) Settings.playersForIndexRead.get(player.Index);
            view.setX(player.PositionX);
            view.setY(player.PositionY);
        }
        if(FrameBuffer.PlayerPositionInFrame.BallParentIndex != -1){
            Settings.ParentBall = (View) Settings.playersForIndexRead.get(FrameBuffer.PlayerPositionInFrame.BallParentIndex);
        }
        paintConttroler.ClearPaint();
    }

    public void ResetPlayerPosition() {
        if(FrameBuffer.PlayerPositionInFrame == null){return;}
        RemovePlayerPosition();
        for(int i = 0; i < ietterator.GetValue(); i++){
            FrameBuffer.FrameUnit frameUnit = FrameBuffer.Frames.get(i);
            switch(frameUnit.Type){
                case paint:
                    FrameBuffer.PaintUnit paintUnit = frameUnit.paint;
                    paintConttroler.ViewPaintsInFrame(paintUnit);
                    break;
                case movePlayer:
                    FrameBuffer.MotionPlayer motionPlayer = frameUnit.frame;
                    View player = (View) Settings.playersForIndexRead.get(motionPlayer.Index);
                    if(motionPlayer.BallParentIndex != -1){
                        Settings.ParentBall = (View) Settings.playersForIndexRead.get(motionPlayer.BallParentIndex);
                    }
                    if(motionPlayer.Index == motionPlayer.BallParentIndex){
                        float DeltaBallX = player.getX() - Settings.ball.getX();
                        float DeltaBallY = player.getY() - Settings.ball.getY();
                        Settings.ball.setX(motionPlayer.PositionX - DeltaBallX);
                        Settings.ball.setY(motionPlayer.PositionY - DeltaBallY);
                    }
                    player.setX(motionPlayer.PositionX);
                    player.setY(motionPlayer.PositionY);

                    break;
            }
        }
    }

    public void OnStartAnimation() {
        if(Settings.isPlayAnimation){return;}
        if (ietterator.GetValue() == ietterator.GetMaxValue()) {
            ietterator.SetFirstValue();
            ResetPlayerPosition(); }
        animationComponent.OnStartAnimation(new SoftReference<>(ietterator));
    }
    public void OnStopAnimation(){
        if(!Settings.isPlayAnimation) {return;}
        animationComponent.OnStopAnimation();
        ResetPlayerPosition();
    }

    public void OnStartAnimation(HBRecorder hbRecorder)
    {
        if(ietterator.GetValue() == ietterator.GetMaxValue()){ietterator.SetFirstValue(); ResetPlayerPosition();}
        animationComponent.OnStartAnimation(new SoftReference<>(ietterator), hbRecorder);
    }
    public void OnPauseAnimation()
    {
        animationComponent.OnPauseAnimation();
    }
    public void OnResumeAnimation()
    {
        animationComponent.OnResumeAnimation();
    }

    public static void ResetIterator(){
        ietterator.SetFirstValue();
        ietterator.SetMaxValue(FrameBuffer.Frames.size());
    }


}
