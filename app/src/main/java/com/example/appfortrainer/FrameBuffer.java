package com.example.appfortrainer;

import com.example.appfortrainer.paint.PaintConttroler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FrameBuffer implements Serializable{

    public enum TypePlayer{
        mainPlayer, enemyPlayer, ball;
    }

    public final static class MotionPlayer{
        public int Index;
        public int BallParentIndex;
        public float PositionX;
        public float PositionY;
    };

    public enum TypeFrame{
        paint, movePlayer;
    }

    public final static class PlayerPosition implements Serializable {public List<MotionPlayer> playerPosition = new ArrayList(); public int BallParentIndex;}
    public static class PaintUnit { public String text; public PaintConttroler.TypePaint Type; public List<PaintConttroler.Vector2> ListPoints = new ArrayList<>();}
    public static class FrameUnit{public TypeFrame Type; public MotionPlayer frame; public PaintUnit paint;}
    public static List<FrameUnit> Frames = new ArrayList();
    public static PlayerPosition PlayerPositionInFrame;
    public static List<PlayerInformation> playerInformations = new ArrayList<>();

    public static void ResetBuffer() {
        Frames = new ArrayList<>();
        PlayerPositionInFrame = null;
        playerInformations = new ArrayList<>();
    }

    public final static class PlayerInformation{
        public int Index;
        public int Number;
        public String Name;
        public TypePlayer typePlayer;
    }


    public final static class Vector2{
        private float X = 0.0f, Y = 0.0f;
        public Vector2(final float x, final float y){
            X = x; Y = y;
        }
        public float getX() {
            return X;
        }
        public float getY() {
            return Y;
        }

        public void setX(float x) {
            X = x;
        }
        public void setY(float y) {
            Y = y;
        }
    }
}