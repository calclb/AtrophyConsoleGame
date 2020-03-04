package me.ubuntoof.events.globalconditions;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.handlers.Battle;
import me.ubuntoof.modifiers.GlobalCondition;
import me.ubuntoof.utils.Colorizer;

public class GlobalConditionTriggerEvent extends Event implements Cancellable
{
    private boolean cancelled;
    public final Battle battle;
    public final GlobalCondition gc;

    public GlobalConditionTriggerEvent(Battle b, GlobalCondition globalCondition)
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
        for(Actor a : battle.getLivingCombatants()) battle.battleInteractionsHandler.registerEvent(new GlobalConditionApplicationEvent(gc, a));

        if(gc.getDurationInTurns() == 0) System.out.println(Colorizer.BOLD + Colorizer.LIGHT_BLUE + gc.icon + " The hail subsided." + Colorizer.RESET);
        gc.decrementTurnsRemaining();
    }
}
