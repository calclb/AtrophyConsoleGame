package me.ubuntoof.passives;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.listeners.BattleInteractions;

public abstract class Passive implements BattleInteractions
{
    protected String header;
    protected Actor owner; // imitates behavior of final variable

    public void initOwner(Actor actor)
    {
        if(owner == null) owner = actor;
    }

    public abstract void activate();
}
