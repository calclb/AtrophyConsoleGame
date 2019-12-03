package me.ubuntoof.Modifiers;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Utils.TextFormatter;

public abstract class Ailment extends Decrementable {

    protected String icon;
    protected String name;
    protected String description;

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

    public String getIcon() { return icon; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public abstract void applyEffects(Actor target);

    public String toString() { return TextFormatter.formatAilment(icon, name, description); }
}
