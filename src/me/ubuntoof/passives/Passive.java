package me.ubuntoof.passives;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.handlers.BattleInteractions;

public abstract class Passive implements BattleInteractions
{
    protected String header;
    protected Actor owner; // imitates behavior of final variable
    protected boolean killed;

    public void initOwner(Actor actor)
    {
        if(owner == null) owner = actor;
    }

    // TODO consider refactoring
    public void kill() { killed = true; }
    public boolean getKilled() { return killed; }
}
