package me.ubuntoof.events.statmodifiers;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.modifiers.StatModifier;

public class StatModifierAddEvent implements Event, Cancellable
{
    private boolean cancelled;
    public StatModifier sm;
    public Actor actor;

    public StatModifierAddEvent(StatModifier sm, Actor actor)
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
        boolean existingModifier = false;
        for(StatModifier s : actor.getStatModifiers()) if(sm.getModifierType().equals(s.getModifierType()))
        {
            s.setMultiplier(s.getMultiplier() * sm.getMultiplier());
            existingModifier = true;
            break;
        }
        if(!existingModifier) actor.getStatModifiers().add(sm);
    }
}
