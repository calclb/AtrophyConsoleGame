package me.ubuntoof.Modifiers.Ailments;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Modifiers.Ailment;
import me.ubuntoof.Modifiers.Decrementable;
import me.ubuntoof.Modifiers.StatModifier;
import me.ubuntoof.Stats;
import me.ubuntoof.Utils.Colorizer;

import java.util.Random;

public class Paralysis extends Ailment {

    Random random = new Random();

    public Paralysis(int durationInTurns)
    {
        super(Colorizer.YELLOW + "\uD83D\uDDF2", Colorizer.YELLOW + "Paralysis", "Reduces target speed and has a chance of cancelling their actions.", durationInTurns);
    }

    @Override
    public void applyEffects(Actor actor)
    {
        actor.getStatModifiers().add(new StatModifier(Stats.SPEED, (2/3d), getDurationInTurns()));
        if(random.nextInt(2) < 1) {
            actor.setEligibleToAct(false);
            System.out.println(icon + " " + Colorizer.YELLOW + actor.getName() + "'s nerves rendered unresponsive." + Colorizer.RESET);
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Paralysis;
    }
}
