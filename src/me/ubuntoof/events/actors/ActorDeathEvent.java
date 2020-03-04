package me.ubuntoof.events.actors;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;

public class ActorDeathEvent extends Event
{
    public final Actor actor;

    public ActorDeathEvent(Actor actor)
    {
        this.actor = actor;
    }

    @Override public void perform() {}
}
