package me.ubuntoof.actions;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.Stats;
import me.ubuntoof.utils.Colorizer;

public class Bop extends Action
{
    public Bop()
    {
        super(ActionType.ATTACK, "Bop", "Deals damage equal to user's " + Stats.STRENGTH.getName() + ".", false, true);
    }

    @Override public void commit(Actor user, Actor target)
    {
        int dmg = target.takeDamage(user.getStrength());
        user.getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
    }
}
