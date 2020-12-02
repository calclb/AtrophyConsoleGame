package me.ubuntoof.passives;

import me.ubuntoof.Stats;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.state.GlobalTurnEndEvent;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;

import java.text.NumberFormat;
import java.util.Random;

public class Inflame extends Condition {
    private final Random random = new Random();

    private static final NumberFormat pf = NumberFormat.getPercentInstance();
    private static final int turnDuration = 2;
    private static final double mod = 1.2d;

    public Inflame()
    {
        super("Inflame", "Applies a random stat buff to user every turn (lasts 2 turns) which increases said stat by " + pf.format(mod - 1) + ".");
    }

    public String getHeader() {
        return Colorizer.LIGHT_BLUE + name + "〉" + Colorizer.RESET;
    }

    public void activate(Actor owner)
    {
        Stats randomStat = Stats.VALUES.get(random.nextInt(Stats.SIZE));

        String msg = (getHeader() + owner + "'s " + randomStat.getName() + " increased by " + pf.format(mod - 1) + " for " + turnDuration + " turns.\n");
        owner.battle.println(msg);
        owner.addStatModifier(new StatModifier(randomStat, mod, turnDuration));
    }


    /**
     * @param owner The Actor to run with the Condition
     * @param e     The event to analyze
     */
    @Override public void on(Actor owner, Event e)
    {
        if(!(e instanceof GlobalTurnEndEvent)) return;
        if(owner.isAlive()) activate(owner);
    }
}
