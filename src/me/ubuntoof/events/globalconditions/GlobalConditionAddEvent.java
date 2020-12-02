package me.ubuntoof.events.globalconditions;

import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.handlers.Battle;
import me.ubuntoof.modifiers.GlobalCondition;

public class GlobalConditionAddEvent extends Event implements Cancellable
{
    private boolean cancelled;
    public final Battle battle;
    public final GlobalCondition gc;
    public final int turns;

    public GlobalConditionAddEvent(Battle b, GlobalCondition globalCondition, int t)
    {
        battle = b;
        gc = globalCondition;
        turns = t;
    }

    @Override public boolean isDisallowed() { return cancelled; }
    @Override public void disallow() { cancelled = true; }

    @Override public void perform()
    {
        battle.addGlobalCondition(gc, turns);
        battle.println(gc.appearMessage);
    }
}
