package me.ubuntoof.actions.spaelcaster;

import me.ubuntoof.actions.Action;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.modifiers.ailments.Paralysis;
import me.ubuntoof.utils.Colorizer;

public class Sting extends Action
{
    private static final int turns = 3;
    public Sting()
    {
        super(ActionType.PIERCING, "Sting", "Deals damage equal to user's speed, and has a chance of Paralyzing the target.", false, true);
    }

    @Override public void commit(Actor user, Actor target)
    {
        ActorDamageEvent ade = target.takeDamage(user.getAgility());
        boolean doesParalysisApply = random.nextInt(5) < 2;

        user.battle.print(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage"
                + (doesParalysisApply ? "" : ".\n"));

        if(doesParalysisApply)
        {
            Paralysis paralysis = new Paralysis(turns);
            user.battle.println(" and inflicting " + paralysis.name + Colorizer.RESET + " for " + turns + " turns.");
            target.addAilment(paralysis);
        }
    }
}
