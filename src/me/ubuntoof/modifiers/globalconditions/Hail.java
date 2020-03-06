package me.ubuntoof.modifiers.globalconditions;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.listeners.BattleInteractions;
import me.ubuntoof.modifiers.GlobalCondition;
import me.ubuntoof.utils.Colorizer;

public class Hail extends GlobalCondition implements BattleInteractions
{
    public Hail()
    {
        super(Colorizer.LIGHT_BLUE + "❄", Colorizer.LIGHT_BLUE + "Hail", "Deals 1/16 of a combatant's health as damage.", 4,
                Colorizer.BOLD + Colorizer.LIGHT_BLUE + "❄ Hail begins to pelt the combatants.\n" + Colorizer.RESET);
    }

    @Override public void applyEffects(Actor actor)
    {
        if(actor.isAlive())
        {
            int dmg = actor.takeDamage((int)Math.max(actor.getMaxHealth()/16d, 1), true);
            actor.getBattle().println(icon + " " + actor.getAndFormatThisCombatantIndex() + " " + actor.getName() + Colorizer.LIGHT_BLUE + " was buffeted by hail, taking " + Colorizer.RED + dmg + Colorizer.LIGHT_BLUE + " damage." + Colorizer.RESET);
        }
    }

    @Override public boolean equals(Object o) {
        return o instanceof Hail;
    }
}
