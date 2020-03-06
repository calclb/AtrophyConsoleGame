package me.ubuntoof.passives;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.ailments.AilmentAddEvent;
import me.ubuntoof.utils.Colorizer;

public class Immunity extends Passive
{
    public Immunity()
    {
        header = Colorizer.LIGHT_BLUE + "Immunity〉" + Colorizer.RESET;
    }

    @Override public void activate()
    {
        String msg = (header + owner + " is immune to ailments.");
        owner.getBattle().println(msg);
    }

    @Override public void notifyEvent(Event e) // called by the Actor class
    {
        if(!owner.isAlive() || !(e instanceof AilmentAddEvent)) return;
        AilmentAddEvent aae = (AilmentAddEvent) e;
        if (aae.actor != owner) return;
        activate();
        aae.setCancelled(true);
    }
}
