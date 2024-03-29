package me.ubuntoof.events.statmodifiers;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.modifiers.StatModifier;

public class StatModifierRemoveEvent implements Event, Cancellable
{
    private boolean cancelled;
    public StatModifier sm;
    public Actor actor;

    public StatModifierRemoveEvent(StatModifier sm, Actor actor)
    {
        this.sm = sm;
        this.actor = actor;
    }

    @Override public boolean isDisallowed()
    {
        return cancelled;
    }

    @Override public void disallow()
    {
        cancelled = true;
    }

    @Override public void perform()
    {
        actor.getStatModifiers().remove(sm);
    }
}
