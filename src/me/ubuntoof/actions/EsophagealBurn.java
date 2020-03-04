package me.ubuntoof.actions;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.utils.Colorizer;

public class EsophagealBurn extends Action
{
    public EsophagealBurn()
    {
        super(ActionType.ATTACK, "Esophageal Burn", "Deals notable damage to a target in return for recoil damage dealt to you.", false, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        int dmg = target.takeDamage((int)(user.getStrength() * 1.5d));
        int recoil_dmg = user.takeDamage(dmg/3 + random.nextInt(dmg/4), true);
        String msg = "";
        msg += (user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.\n");
        msg += (user + " took " + Colorizer.RED + recoil_dmg + Colorizer.RESET + " recoil damage.");
        user.getBattle().println(msg);
    }
}
