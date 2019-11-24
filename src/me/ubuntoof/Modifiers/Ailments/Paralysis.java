package me.ubuntoof.Modifiers.Ailments;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Modifiers.Ailment;
import me.ubuntoof.Modifiers.StatModifier;
import me.ubuntoof.Stats;

import java.util.Random;

public class Paralysis extends Ailment {

    Random random = new Random();

    Paralysis(int durationInTurns)
    {
        super("\uD83D\uDDF2", "Paralysis", "Reduces target speed and has a chance of cancelling their actions.", durationInTurns);
    }

    @Override
    public void applyEffects(Actor actor)
    {
        actor.getStatModifiers().add(new StatModifier(Stats.SPEED, (1/3d), getDurationInTurns()));
        if(random.nextInt(2) < 1); // TODO add action cancelling when turns run
    }
}
