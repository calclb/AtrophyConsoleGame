package me.ubuntoof.actions;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.utils.Colorizer;

public class Rejuvenate extends Action {

    public Rejuvenate()
    {
        super(ActionType.STATUS, "Rejuvenate", "Heals target health by user's Strength.", true, false, true);
    }

    @Override public void commit(Actor user, Actor target)
    {
        target.setHealth(Math.min(target.getStamina(), target.getHealth() + user.getStrength()));
        user.battle.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", healing "
                + Colorizer.GREEN + user.level / 3 + Colorizer.RESET + " health.");
    }
}
