package me.ubuntoof.actions.golem;

import me.ubuntoof.actions.Action;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.utils.Colorizer;

public class Obliderate extends Action
{
    public Obliderate()
    {
        super(ActionType.BLUDGEONING, "Obliderate", "Deals damage equal to half the user's Strength.", false, true);
    }

    @Override public void commit(Actor user, Actor target)
    {
        ActorDamageEvent ade = target.takeDamage((int)(user.getStrength() * 0.5d), false);
        user.battle.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage.");
    }
}
