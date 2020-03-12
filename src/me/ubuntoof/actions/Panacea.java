package me.ubuntoof.actions;

import me.ubuntoof.Stats;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.statmodifiers.StatModifierAddEvent;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;

public class Panacea extends Action
{
    public final int cooldownInTurns = 3;

    public Panacea()
    {
        super(ActionType.STATUS, "Panacea", "Increases " + Stats.STAMINA.getName() + " by 50% for 3 turns.", true, false, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        int cd = user.getActionCooldown(this);
        if(cd > 0)
        {
            user.getBattle().println(user + " attempted to use " + getName() + ", but it had no effect. " +
                    Colorizer.ITALIC + Colorizer.GRAY + "(On cooldown for " + cd + " turns.)" + Colorizer.RESET);
            return;
        }

        target = user;

        int hpIncrease = (int)(user.getStamina() * 1.5d);
        StatModifier maxHealthModifier = new StatModifier(Stats.STAMINA, 1.5d, 3);
        user.setHealth(Math.min(user.getStamina(), user.getHealth() + hpIncrease));

        user.getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", increasing " + Stats.STAMINA.getName() + " by 50% for "
                            + maxHealthModifier.getDurationInTurns() + " turns." + Colorizer.RESET);

        target.addStatModifier(maxHealthModifier);

        user.addActionCooldown(this, cooldownInTurns);
    }
}
