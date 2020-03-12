package me.ubuntoof.events.globalconditions;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.modifiers.GlobalCondition;

public class GlobalConditionApplicationEvent implements Event, Cancellable
{

    private boolean cancelled;
    public final GlobalCondition gc;
    public final Actor actor;

    public GlobalConditionApplicationEvent(GlobalCondition gc, Actor actor)
    {
        this.gc = gc;
        this.actor = actor;
    }

    @Override public boolean isDisallowed() { return cancelled; }
    @Override public void disallow() { cancelled = true; }

    @Override public void perform()
    {
        gc.applyEffects(actor);
    }
}
