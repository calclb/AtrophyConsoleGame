package me.ubuntoof.actions.goblin;

import me.ubuntoof.actions.Action;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.utils.Colorizer;

public class Pummel extends Action
{
    public Pummel()
    {
        super(ActionType.BLUDGEONING, "Pummel", "Deals damage equal to half the user's level.", false, true);
    }

    @Override public void commit(Actor user, Actor target)
    {
        ActorDamageEvent ade = target.takeDamage((int)(user.level * 0.5d), false);
        user.battle.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage.");
    }
}
