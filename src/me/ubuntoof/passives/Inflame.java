package me.ubuntoof.passives;

import me.ubuntoof.Stats;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.state.GlobalTurnEndEvent;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;

import java.text.NumberFormat;
import java.util.Random;

public class Inflame extends Passive
{
    private final NumberFormat pf = NumberFormat.getPercentInstance();
    private final Random random = new Random();

    public Inflame()
    {
        header = Colorizer.LIGHT_RED + "Inflame〉" + Colorizer.RESET;
    }

    public void activate()
    {
        Stats randomStat = Stats.VALUES.get(random.nextInt(Stats.SIZE));
        double mod = 1.2d;

        int turnDuration = 2;

        String msg = (header + owner + "'s " + randomStat.getName() + " increased by " + pf.format(mod - 1) + " for " + turnDuration + " turns.\n");
        owner.getBattle().println(msg);
        owner.addStatModifier(new StatModifier(randomStat, mod, turnDuration));
    }

    public void onEvent(GlobalTurnEndEvent e) // called by the Actor class
    {
        if(owner.isAlive()) activate();
    }
}
