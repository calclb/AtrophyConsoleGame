package me.ubuntoof.actions.goblin;

import me.ubuntoof.Stats;
import me.ubuntoof.actions.Action;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.statmodifiers.StatModifierAddEvent;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;

public class Intimidate extends Action
{
    private final int turns = 2;
    public Intimidate()
    {
        super(ActionType.STATUS, "Intimidate", "Reduces Strength of target by 25% for 2 turns.", false, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        StatModifier sm = new StatModifier(Stats.STRENGTH, 0.75d, turns);
        user.battle.registerEvent(new StatModifierAddEvent(target, sm));
        user.battle.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) +
                ", applying " + sm + Colorizer.RESET + " to " + target + " for " + turns + " turns.");
    }
}
