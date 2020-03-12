package me.ubuntoof.modifiers.ailments;

import me.ubuntoof.Stats;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.modifiers.Ailment;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;

import java.util.Random;

public class Paralysis extends Ailment
{
    Random random = new Random();

    public Paralysis(int durationInTurns)
    {
        super(Colorizer.YELLOW + "\uD83D\uDDF2", Colorizer.YELLOW + "Paralysis", "Reduces target speed and has a chance of cancelling their actions.", durationInTurns);
    }

    @Override public void applyEffects(Actor actor)
    {
        actor.addStatModifier(new StatModifier(Stats.AGILITY, (2/3d), getDurationInTurns()));
        if(!actor.getEligibleToAct()) return;
        if(random.nextInt(2) < 1) {
            actor.setEligibleToAct(false);
            actor.getBattle().println(icon + " " + Colorizer.YELLOW + actor.getName() + "'s nerves rendered unresponsive." + Colorizer.RESET);
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Paralysis;
    }
}
