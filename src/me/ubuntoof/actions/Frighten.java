package me.ubuntoof.actions;

import me.ubuntoof.Stats;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.events.statmodifiers.StatModifierAddEvent;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;

public class Frighten extends Action
{
    public Frighten()
    {
        super(ActionType.ATTACK, "Frighten", "Lowers target's " + Stats.DEXTERITY.getName(), false, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        StatModifier sm = new StatModifier(Stats.DEXTERITY, 0.65d, 3);
        user.getBattle().battleInteractionsHandler.registerEvent(new StatModifierAddEvent(sm, target));
        user.getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) +
                ", applying " + sm + Colorizer.RESET + " to " + target + " for " + sm.getDurationInTurns() + " turns.");
    }
}
