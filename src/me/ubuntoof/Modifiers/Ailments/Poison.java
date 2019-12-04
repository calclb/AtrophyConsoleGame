package me.ubuntoof.Modifiers.Ailments;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Modifiers.Ailment;
import me.ubuntoof.Utils.Colorizer;

public class Poison extends Ailment {

    private int turns = 0;

    public Poison(int durationInTurns) {
        super(Colorizer.PURPLE + "\uD83D\uDC0D", "Poison", "Inflicts damage equal to 6.25% of the victim's health, increasing in damage per turn.", durationInTurns);
    }

    @Override
    public void applyEffects(Actor actor) {
        int dmg = (int)Math.max(actor.getMaxHealth()/(Math.max(16d-2*turns, 3d)), 1);
        System.out.println(Colorizer.PURPLE + getIcon() + " " + actor + " took " + Colorizer.RED + dmg + Colorizer.PURPLE + " damage to " + getName() + ".");
        actor.takeDamage(dmg);
        turns++;
        decrementTurnsRemaining();
    }
}