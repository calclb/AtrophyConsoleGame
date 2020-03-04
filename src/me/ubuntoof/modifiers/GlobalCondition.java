package me.ubuntoof.modifiers;

import me.ubuntoof.characters.Actor;

public abstract class GlobalCondition extends Decrementable
{
    public final String icon;
    public final String name;
    public final String description;
    public final String appearMessage;

    protected GlobalCondition(String icon, String name, String description, String appearMessage)
    {
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.appearMessage = appearMessage;
        setPermanent();
    }

    protected GlobalCondition(String icon, String name, String description, int duration, String appearMessage)
    {
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.appearMessage = appearMessage;
        setDurationInTurns(duration);
    }

    public abstract void applyEffects(Actor target);

    @Override public abstract boolean equals(Object o);
}
