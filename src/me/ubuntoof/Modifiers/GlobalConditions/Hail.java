package me.ubuntoof.Modifiers.GlobalConditions;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Modifiers.GlobalCondition;
import me.ubuntoof.Utils.Colorizer;

public class Hail extends GlobalCondition {

    public Hail()
    {
        super(Colorizer.LIGHT_BLUE + "❄", Colorizer.LIGHT_BLUE + "Hail", "Deals 1/16 of a combatant's health as damage.", 4);
        System.out.println(Colorizer.BOLD + Colorizer.LIGHT_BLUE + icon + " Hail begins to pelt the combatants." + Colorizer.RESET);
    }

    @Override
    public void applyEffects(Actor[] targets){
        for(Actor actor : targets) if(actor.isAlive())
        {
            int dmg = actor.takeDamage((int)Math.max(actor.getMaxHealth()/16d, 1), true);
            System.out.println(Colorizer.LIGHT_BLUE + icon + " " + actor + " was buffeted by hail, taking " + Colorizer.RED + dmg + Colorizer.LIGHT_BLUE + " damage." + Colorizer.RESET);

        }

        decrementTurnsRemaining();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Hail;
    }
}
