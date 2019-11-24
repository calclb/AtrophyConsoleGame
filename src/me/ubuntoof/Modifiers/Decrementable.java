package me.ubuntoof.Modifiers;

public class Decrementable {

    public final int TILL_END_OF_TURN = 0;

    private boolean isPermanent = false;
    private int durationInTurns = 0;

    public void setPermanent()
    {
        isPermanent = true;
        durationInTurns = Integer.MAX_VALUE;
    }

    public void setPermanent(boolean condition)
    {
        if(condition)
        {
            isPermanent = true;
            durationInTurns = Integer.MAX_VALUE;
        }
        else
        {
            isPermanent = false;
            durationInTurns = TILL_END_OF_TURN;
        }
    }

    public void decrementTurnsRemaining() { if(!isPermanent) durationInTurns--; }
    public void setDurationInTurns(int duration)
    {
        durationInTurns = duration;
        isPermanent = false;
    }
    public boolean isPermanent() { return isPermanent; }
    public int getDurationInTurns() { return durationInTurns; }
}
