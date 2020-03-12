package me.ubuntoof.actions;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.events.ailments.AilmentAddEvent;
import me.ubuntoof.modifiers.ailments.Paralysis;
import me.ubuntoof.utils.Colorizer;

public class Taze extends Action
{
    public Taze()
    {
        super(ActionType.ATTACK, "Taze", "Deals low damage, but guarantees Paralysis infliction.", false, true);
    }

    @Override
    public void commit(Actor user, Actor target)
    {
        ActorDamageEvent ade = target.takeDamage(user.getStrength()/3);

        Paralysis paralysis = new Paralysis(1);

        user.getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET +
                " damage and inflicting " + paralysis.name + Colorizer.RESET + " for " + paralysis.getDurationInTurns() + (paralysis.getDurationInTurns() == 1 ? " turn." : " turns."));

        user.getBattle().battleInteractionsHandler.registerEvent(new AilmentAddEvent(paralysis, target));
    }
}
