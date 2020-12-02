package me.ubuntoof.actions;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.utils.Colorizer;

public class Panic extends Action
{
    public Panic() {
        super(ActionType.FORCE, "Panic", "Attack a random target (including yourself or an ally) for massive damage.", false, true, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        String msg = "";
        int targetIndex = random.nextInt(user.battle.getLivingCombatants().size());
        target = user.battle.getLivingCombatants().get(targetIndex);
        ActorDamageEvent ade = target.takeDamage(random.nextInt(user.level + target.level));
        msg += (user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage.");
        user.battle.println(msg);
    }
}
