package me.ubuntoof.modifiers;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.passives.Condition;
import me.ubuntoof.utils.Colorizer;

public abstract class Ailment extends Condition
{
    public final String icon;

    public Ailment(String icon, String name, String description)
    {
        super(name, description);
        this.icon = icon;
    }

    public Ailment(String name, String description)
    {
        super(name, description);
        this.icon = "";
    }

    /**
     * Called when the corresponding {@code AilmentTriggerEvent} is validated by the battle's Actors.
     */
    public abstract void applyEffects(Actor target);

    public String toString()
    {
        // TODO consider delegating formatting responsibility to actor, who has information regarding their ailments
        return Colorizer.FRAME + (icon.isEmpty() ? name : icon) + " (" + getDurationInTurns() + "t)" + Colorizer.RESET;
    }

    @Override public abstract boolean equals(Object o);
}
