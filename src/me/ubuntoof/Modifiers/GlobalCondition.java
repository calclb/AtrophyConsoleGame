package me.ubuntoof.Modifiers;

import me.ubuntoof.Characters.Actor;

public abstract class GlobalCondition extends Decrementable {

    protected String icon;
    protected String name;
    protected String description;

    protected GlobalCondition()
    {
        setPermanent();
    }

    protected GlobalCondition(int duration)
    {
       setDurationInTurns(duration);
    }

    public abstract void applyEffects(Actor[] targets);

    public String getIcon() { return icon; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
