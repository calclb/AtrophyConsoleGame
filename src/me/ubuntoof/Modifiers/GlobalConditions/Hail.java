package me.ubuntoof.Modifiers.GlobalConditions;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Modifiers.GlobalCondition;
import me.ubuntoof.Utils.Colorizer;

public class Hail extends GlobalCondition {

    private static String icon = "❄";

    Hail()
    {
        super(4);
    }

    @Override
    public void applyEffects(Actor[] targets){
        for(Actor actor : targets)
        {
            System.out.println(Colorizer.LIGHT_BLUE + icon + Colorizer.RESET + actor + " was buffeted by hail.");
            actor.takeDamage(actor.getMaxHealth()/16d, true);
            decrementTurnsRemaining();
        }
    }
}
