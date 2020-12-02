package me.ubuntoof.actions.druid.druidimite;

import me.ubuntoof.actions.Action;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.utils.Colorizer;

public class Bonk extends Action
{
    public Bonk()
    {
        super(Action.ActionType.BLUDGEONING, "Bonk", "Deals 3 true damage.", false, true);
    }

    @Override public void commit(Actor user, Actor target)
    {
        ActorDamageEvent ade = target.takeDamage(3, true);
        user.battle.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage.");

    }
}
