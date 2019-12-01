package me.ubuntoof.Modifiers.GlobalConditions;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Modifiers.GlobalCondition;
import me.ubuntoof.Utils.Colorizer;

public class Hail extends GlobalCondition {

    private static String icon = "❄";

    public Hail()
    {
        super(4);
        System.out.println(Colorizer.BOLD + Colorizer.LIGHT_BLUE + icon + " Hail begins to pelt the combatants." + Colorizer.RESET);
    }

    @Override
    public void applyEffects(Actor[] targets){
        for(Actor actor : targets) if(actor.isAlive())
        {
            System.out.println(Colorizer.LIGHT_BLUE + icon + " " + actor + " was buffeted by hail." + Colorizer.RESET);
            actor.takeDamage((int)Math.max(actor.getMaxHealth()/16d, 1), true);
            decrementTurnsRemaining();
        }
    }
}
