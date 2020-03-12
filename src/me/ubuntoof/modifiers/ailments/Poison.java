package me.ubuntoof.modifiers.ailments;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.modifiers.Ailment;
import me.ubuntoof.utils.Colorizer;

public class Poison extends Ailment
{
    private int turns = 0;

    public Poison(int durationInTurns) {
        super(Colorizer.PURPLE + "\uD83D\uDC0D", Colorizer.PURPLE + "Poison", "Inflicts damage equal to 6.25% of the victim's health, increasing in damage per turn.", durationInTurns);
    }

    @Override public void applyEffects(Actor actor)
    {
        int dmg = (int)Math.max(actor.getStamina()/(Math.max(16d-2*turns, 3d)), 1);
        actor.getBattle().println(icon + " " + Colorizer.PURPLE + actor.getName() + " took " + Colorizer.RED + dmg + Colorizer.PURPLE + " damage to " + name + "." + Colorizer.RESET);
        actor.takeDamage(dmg);
        turns++;
        decrementTurnsRemaining();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Poison;
    }
}
