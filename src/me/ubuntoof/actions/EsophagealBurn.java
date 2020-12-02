package me.ubuntoof.actions;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.utils.Colorizer;

public class EsophagealBurn extends Action
{
    public EsophagealBurn()
    {
        super(ActionType.CORROSION, "Esophageal Burn", "Deals notable damage to a target in return for recoil damage dealt to you.", false, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        ActorDamageEvent ade = target.takeDamage((int)(user.getStrength() * 1.5d));
        ActorDamageEvent recoil = user.takeDamage(ade.getDamage()/3 + random.nextInt(Math.max(ade.getDamage()/4, 1)), true);
        String msg = "";
        msg += (user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage.\n");
        msg += (user + " took " + Colorizer.RED + recoil.getDamage() + Colorizer.RESET + " recoil damage.");
        user.battle.println(msg);
    }
}
