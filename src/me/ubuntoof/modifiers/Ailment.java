package me.ubuntoof.modifiers;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.utils.TextFormatter;

public abstract class Ailment extends Decrementable
{
    public final String icon;
    public final String name;
    public final String description;

    public Ailment(String icon, String name, String description, boolean permanent)
    {
        this.icon = icon;
        this.name = name;
        this.description = description;

        if(permanent) setPermanent();
        else setDurationInTurns(0);
    }

    public Ailment(String icon, String name, String description, int duration)
    {
        this.icon = icon;
        this.name = name;
        this.description = description;
        setDurationInTurns(duration);
    }

    public abstract void applyEffects(Actor target);

    public String toString() { return TextFormatter.formatAilment(icon, name, description); }

    @Override public abstract boolean equals(Object o);
}
