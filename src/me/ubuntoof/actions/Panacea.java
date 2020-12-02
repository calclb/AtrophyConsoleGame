package me.ubuntoof.actions;

import me.ubuntoof.Stats;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;

import java.text.NumberFormat;

public class Panacea extends Action
{
    private static final int cooldownInTurns = 3;
    private static final int turns = 3;
    private static final double mod = 1.5d;
    private static final NumberFormat pf = NumberFormat.getPercentInstance();

    public Panacea()
    {
        super(ActionType.STATUS, "Panacea", "Increases " + Stats.STAMINA.getName() + " by " + pf.format(1 - mod) + " for " + turns + " turns.",
                true, false, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        int cd = user.getActionCooldown(this);
        if(cd > 0)
        {
            user.battle.println(user + " attempted to use " + getName() + ", but it had no effect. " +
                    Colorizer.ITALIC + Colorizer.GRAY + "(On cooldown for " + cd + " turns.)" + Colorizer.RESET);
            return;
        }

        target = user;

        int hpIncrease = (int)(user.getStamina() * 1.5d);
        StatModifier maxHealthModifier = new StatModifier(Stats.STAMINA, 1.5d, turns);
        user.setHealth(Math.min(user.getStamina(), user.getHealth() + hpIncrease));

        user.battle.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", increasing " + Stats.STAMINA.getName() + " by 50% for "
                            + turns + " turns." + Colorizer.RESET);

        target.addStatModifier(maxHealthModifier);
        user.addActionCooldown(this, cooldownInTurns);
    }
}
