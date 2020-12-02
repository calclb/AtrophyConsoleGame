package me.ubuntoof.actions;

import me.ubuntoof.Stats;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.statmodifiers.StatModifierAddEvent;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;

public class Frighten extends Action
{
    private final int turns = 3;
    public Frighten()
    {
        super(ActionType.STATUS, "Frighten", "Lowers target's " + Stats.AGILITY.getName(), false, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        StatModifier sm = new StatModifier(Stats.AGILITY, 0.65d, turns);
        user.battle.registerEvent(new StatModifierAddEvent(target, sm));
        user.battle.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) +
                ", applying " + sm + Colorizer.RESET + " to " + target + " for " + turns + " turns.");
    }
}
