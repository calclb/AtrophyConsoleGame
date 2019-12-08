package me.ubuntoof.Modifiers;

import me.ubuntoof.Characters.Actor;

public abstract class GlobalCondition extends Decrementable {

    public final String icon;
    public final String name;
    public final String description;

    protected GlobalCondition(String icon, String name, String description)
    {
        this.icon = icon;
        this.name = name;
        this.description = description;
        setPermanent();
    }

    protected GlobalCondition(String icon, String name, String description, int duration)
    {
        this.icon = icon;
        this.name = name;
        this.description = description;
        setDurationInTurns(duration);
    }

    public abstract void applyEffects(Actor[] targets);

    @Override public abstract boolean equals(Object o);
}
