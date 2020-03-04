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

    public GlobalConditionAddEvent(Battle b, GlobalCondition globalCondition)
    {
        battle = b;
        gc = globalCondition;
    }

    @Override public boolean isCancelled()
    {
        return cancelled;
    }

    @Override public void setCancelled(boolean b)
    {
        cancelled = b;
    }

    @Override public void perform()
    {
        battle.addGlobalCondition(gc);
        battle.println(gc.appearMessage);
    }
}
