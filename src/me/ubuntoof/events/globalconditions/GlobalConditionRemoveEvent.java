package me.ubuntoof.events.globalconditions;

import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.handlers.Battle;
import me.ubuntoof.modifiers.GlobalCondition;

public class GlobalConditionRemoveEvent implements Event, Cancellable
{
    private boolean cancelled;
    public final Battle battle;
    public final GlobalCondition gc;

    public GlobalConditionRemoveEvent(Battle battle, GlobalCondition gc)
    {
        this.battle = battle;
        this.gc = gc;
    }

    @Override public boolean isDisallowed() { return cancelled; }
    @Override public void disallow() { cancelled = true; }

    @Override public void perform()
    {
        battle.removeGlobalCondition(gc);
    }
}
