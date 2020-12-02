package me.ubuntoof.actions.bandit;

import me.ubuntoof.actions.Action;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.utils.Colorizer;

public class ChipAway extends Action
{
    public ChipAway()
    {
        super(ActionType.BLUDGEONING, "Chip Away", "Reduces target HP by 10% as true damage.", false, true, true);
    }

    @Override public void commit(Actor user, Actor target)
    {
        ActorDamageEvent ade = target.takeDamage(Math.max(target.getStamina() / 10, 1), true);
        user.battle.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage.");

    }
}
