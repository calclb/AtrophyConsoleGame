package me.ubuntoof.events.state;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;

public class TurnChangeEvent implements Event
{
    public Actor to;

    public TurnChangeEvent(Actor to)
    {
        this.to = to;
    }

    @Override public void perform()
    {
        to.getBattle().getLivingCombatants().forEach(Actor::onTurnChanged);
        to.onActorTurn();
    }
}
