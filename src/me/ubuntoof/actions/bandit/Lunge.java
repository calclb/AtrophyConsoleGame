package me.ubuntoof.actions.bandit;

import me.ubuntoof.actions.Action;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.utils.Colorizer;

public class Lunge extends Action
{
    public Lunge()
    {
        super(ActionType.BLUDGEONING, "Lunge", "Has a 25% chance of dealing double damage.", false, true, true);
    }

    @Override public void commit(Actor user, Actor target)
    {
            double randResult = random.nextDouble();
            ActorDamageEvent ade = target.takeDamage((int)((0.25d > randResult ? 2d : 1d) * Math.sqrt((user.level + 2d))));
            user.battle.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage.");
    }
}
