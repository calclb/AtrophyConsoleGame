package me.ubuntoof.events.ailments;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.modifiers.Ailment;

public class AilmentAddEvent implements Event, Cancellable
{
    private boolean cancelled;
    public final Ailment ailment;
    public Actor actor;

    public AilmentAddEvent(Ailment ailment, Actor actor)
    {
        this.ailment = ailment;
        this.actor = actor;
    }

    @Override public boolean isDisallowed() { return cancelled; }
    @Override public void disallow() { cancelled = true; }

    @Override public void perform()
    {
        for(Ailment a : actor.getAilments()) if(ailment.equals(a)) { a.setDurationInTurns(Math.max(a.getDurationInTurns(), ailment.getDurationInTurns())); return; }
        actor.getAilments().add(ailment);
    }
}
