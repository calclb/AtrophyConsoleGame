package me.ubuntoof.passives;

import me.ubuntoof.events.ailments.AilmentAddEvent;
import me.ubuntoof.utils.Colorizer;

public class Immunity extends Passive
{
    public Immunity()
    {
        header = Colorizer.LIGHT_BLUE + "Immunity〉" + Colorizer.RESET;
    }

    public void activate()
    {
        String msg = (header + owner + " is immune to ailments.");
        owner.getBattle().println(msg);
    }

    public void onEvent(AilmentAddEvent e) // called by the Actor class
    {
        if(!owner.isAlive()) return;
        if (e.actor != owner) return;
        activate();
        e.disallow();
    }
}
