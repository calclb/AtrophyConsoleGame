package me.ubuntoof.Actions;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Modifiers.StatModifier;
import me.ubuntoof.Stats;
import me.ubuntoof.Utils.Colorizer;

public class Panacea extends Action {

    int cooldownInTurns = 0;

    public Panacea() {
        super(ActionType.STATUS, "Panacea", "Increases " + Stats.MAX_HEALTH.getName() + " by 50% for 3 turns.", true, false);
    }

    @Override
    public void commit(Actor user, Actor target) {
        if(cooldownInTurns != 0)
        {
            System.out.println(user + " attempted to use " + getName() + " , but it had no effect. " +
                    Colorizer.ITALIC + Colorizer.GRAY + "(On cooldown for " + cooldownInTurns + " turns.)" + Colorizer.RESET);
            return;
        }
        cooldownInTurns = 3;

        target = user;

        StatModifier maxHealthModifier = new StatModifier(Stats.MAX_HEALTH, 1.5d, 3);
        StatModifier healthMod = new StatModifier(Stats.HEALTH, 1.5d, 3);

        System.out.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", increasing " + Stats.MAX_HEALTH.getName() + " by 50% for "
                            + maxHealthModifier.getDurationInTurns() + " turns." + Colorizer.RESET);

        target.getStatModifiers().add(maxHealthModifier);
        target.getStatModifiers().add(healthMod);
    }
}
