package me.ubuntoof.Modifiers;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Utils.TextFormatter;

public abstract class Ailment extends Decrementable {

    public final String icon;
    public final String name;
    public final String description;

    protected Ailment(String icon, String name, String description, boolean permanent)
    {
        this.icon = icon;
        this.name = name;
        this.description = description;

        if(permanent) setPermanent();
        else setDurationInTurns(0);
    }

    protected Ailment(String icon, String name, String description, int duration)
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
