package me.ubuntoof.Actions;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Stats;
import me.ubuntoof.Utils.Colorizer;

public class Bop extends Action {

    public Bop() {
        super(ActionType.ATTACK, "Bop", "Deals damage equal to user's " + Stats.STRENGTH.getName() + ".", true);
    }

    @Override
    public void commit(Actor user, Actor target) {
        int dmg = target.takeDamage(user.getStrength());
        System.out.println(user + " used " + getName() + " on " + target + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
    }
}
