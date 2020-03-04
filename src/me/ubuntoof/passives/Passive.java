package me.ubuntoof.passives;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.listeners.BattleInteractions;

public abstract class Passive implements BattleInteractions
{
    protected String header;
    protected Actor owner;

    public Passive(Actor owner)
    {
        this.owner = owner;
    }

    public abstract void activate();
}
