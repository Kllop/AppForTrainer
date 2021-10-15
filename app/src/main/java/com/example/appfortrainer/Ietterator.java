package com.example.appfortrainer;

public class Ietterator {

    private int ietterator = 0;
    private int MaxValue = 0;
    private int MinValue = 0;

    private int Clamp(final int value, final int min, final int max)
    {
        return Math.max(min, Math.min(max, value));
    }
    public Ietterator(){
        MaxValue = FrameBuffer.Frames.size();
        SetFirstValue();
    }

    public final void NextValue()
    {
        ietterator = Clamp(ietterator + 1, MinValue, MaxValue);
    }
    public final void BackValue()
    {
        ietterator = Clamp(ietterator - 1, MinValue, MaxValue);
    }
    public final int GetValue()
    {
        return ietterator;
    }
    public final int GetMaxValue() { return MaxValue; }
    public final void SetMaxValue(final int maxValue)
    {
        MaxValue = maxValue;
    }
    public final void SetLastValue()
    {
        ietterator = MaxValue;
    }
    public  final void SetFirstValue()
    {
        ietterator = MinValue;
    }
    public final void SetValue(final int value)
    {
        ietterator = Clamp(value, MinValue, MaxValue);
    }
}
