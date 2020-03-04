package me.ubuntoof.events.ailments;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.modifiers.Ailment;

public class AilmentRemoveEvent extends Event implements Cancellable
{
    private boolean cancelled;
    public final Ailment ailment;
    public final Actor actor;

    public AilmentRemoveEvent(Ailment ailment, Actor actor)
    {
        this.ailment = ailment;
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
        actor.getAilments().remove(ailment);
    }
}
