package me.ubuntoof.modifiers;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.handlers.BattleInteractions;

public abstract class GlobalCondition implements BattleInteractions
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
    }

    public abstract void applyEffects(Actor target);

    @Override public abstract boolean equals(Object o);
}
