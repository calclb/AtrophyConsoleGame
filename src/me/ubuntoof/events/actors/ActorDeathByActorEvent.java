package me.ubuntoof.events.actors;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;

public class ActorDeathByActorEvent extends Event
{
    public final Actor actor;
    public final Actor killer;

    public ActorDeathByActorEvent(Actor actor, Actor killer)
    {
        this.actor = actor;
        this.killer = killer;
    }

    @Override public void perform()
    {

    }
}
