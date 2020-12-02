package me.ubuntoof.events.actors;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;
import me.ubuntoof.utils.Colorizer;

public class ActorDeathEvent extends Event
{
    public final Actor actor;

    public ActorDeathEvent(Actor actor)
    {
        this.actor = actor;
    }

    public void onEvent(ActorDamageEvent e)
    {
        if(!e.getActor().isAlive()) perform();
    }

    @Override public void perform() // TODO perform after ActorDamageEvent
    {
        actor.battle.println(Colorizer.RED + Colorizer.REVERSE + Colorizer.BOLD + "\uD83D\uDC80 " + actor.name + " has been eliminated." + Colorizer.RESET);
    }
}
