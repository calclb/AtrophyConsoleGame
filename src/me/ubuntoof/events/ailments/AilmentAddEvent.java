package me.ubuntoof.events.ailments;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.modifiers.Ailment;

public class AilmentAddEvent extends Event implements Cancellable
{
    private boolean cancelled;
    public final Ailment ailment;
    public final Actor actor;

    public AilmentAddEvent(Actor actor, Ailment ailment)
    {
        this.actor = actor;
        this.ailment = ailment;
    }

    @Override public boolean isDisallowed() { return cancelled; }
    @Override public void disallow() { cancelled = true; }

    @Override public void perform()
    {
        actor.getConditions().add(ailment);
    }
}
