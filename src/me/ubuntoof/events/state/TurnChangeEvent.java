package me.ubuntoof.events.state;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;

public class TurnChangeEvent extends Event
{
    public Actor to;

    public TurnChangeEvent(Actor to)
    {
        this.to = to;
        perform();
    }

    @Override public void perform()
    {
        to.onActorTurn();
    }
}
