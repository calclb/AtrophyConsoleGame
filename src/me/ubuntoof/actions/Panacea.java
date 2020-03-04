package me.ubuntoof.actions;

import me.ubuntoof.Stats;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.statmodifiers.StatModifierAddEvent;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;

public class Panacea extends Action
{
    // TODO cooldowns
    int cooldownInTurns = 0;

    public Panacea()
    {
        super(ActionType.STATUS, "Panacea", "Increases " + Stats.MAX_HEALTH.getName() + " by 50% for 3 turns.", true, false, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        if(cooldownInTurns != 0)
        {
            user.getBattle().println(user + " attempted to use " + getName() + ", but it had no effect. " +
                    Colorizer.ITALIC + Colorizer.GRAY + "(On cooldown for " + cooldownInTurns + " turns.)" + Colorizer.RESET);
            return;
        }
        cooldownInTurns = 3;

        target = user;

        StatModifier maxHealthModifier = new StatModifier(Stats.MAX_HEALTH, 1.5d, 3);
        StatModifier healthMod = new StatModifier(Stats.HEALTH, 1.5d, 3);

        user.getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", increasing " + Stats.MAX_HEALTH.getName() + " by 50% for "
                            + maxHealthModifier.getDurationInTurns() + " turns." + Colorizer.RESET);

        user.getBattle().battleInteractionsHandler.registerEvent(new StatModifierAddEvent(maxHealthModifier, target));
        user.getBattle().battleInteractionsHandler.registerEvent(new StatModifierAddEvent(healthMod, target));
    }
}
