package me.ubuntoof.Modifiers;

import me.ubuntoof.Characters.Actor;

public abstract class GlobalCondition extends Decrementable {

    protected GlobalCondition()
    {
        setPermanent();
    }

    protected GlobalCondition(int duration)
    {
       setDurationInTurns(duration);
    }

    public abstract void applyEffects(Actor[] targets);
}
