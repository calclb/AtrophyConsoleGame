package me.ubuntoof.passives;

import me.ubuntoof.Stats;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.ailments.AilmentAddEvent;
import me.ubuntoof.events.state.GlobalTurnEndEvent;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;

import java.text.NumberFormat;
import java.util.Random;

public class Immunity extends Passive
{
    private final NumberFormat pf = NumberFormat.getPercentInstance();
    private final Random random = new Random();

    public Immunity(Actor owner)
    {
        super(owner);
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
