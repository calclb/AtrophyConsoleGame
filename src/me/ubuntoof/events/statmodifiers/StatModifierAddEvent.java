package me.ubuntoof.events.statmodifiers;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.modifiers.StatModifier;

public class StatModifierAddEvent extends Event implements Cancellable
{
    private boolean cancelled;
    public StatModifier sm;
    public Actor actor;

    public StatModifierAddEvent(StatModifier sm, Actor actor)
    {
        this.sm = sm;
        this.actor = actor;
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
        if(!isCancelled()) actor.getStatModifiers().add(sm);
    }
}
