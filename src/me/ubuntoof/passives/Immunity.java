package me.ubuntoof.passives;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.ailments.AilmentAddEvent;
import me.ubuntoof.utils.Colorizer;

public class Immunity extends Condition
{
    public static final String header = Colorizer.LIGHT_BLUE + "Immunity〉" + Colorizer.RESET;
    public static final String description = "Prevents the affliction of ailments.";

    public Immunity()
    {
        super("Immunity", "Prevents the user from being afflicted with ailments.");
    }

    public String getHeader() { return Colorizer.LIGHT_BLUE + name + "〉" + Colorizer.RESET; }

    public void activate(Actor owner)
    {
        owner.battle.println(header + owner + " is immune to ailments.");
    }

    /**
     * @param owner The Actor to run with the Condition
     * @param e     The event to analyze
     */
    @Override public void on(Actor owner, Event e)
    {
        if(!(e instanceof AilmentAddEvent)) return;
        AilmentAddEvent aae = (AilmentAddEvent) e;
        if(!owner.isAlive() || aae.actor != owner) return;
        activate(owner);
        aae.disallow();
    }
}
