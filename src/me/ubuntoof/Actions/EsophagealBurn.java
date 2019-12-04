package me.ubuntoof.Actions;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Utils.Colorizer;

public class EsophagealBurn extends Action {

    public EsophagealBurn() {
        super(ActionType.ATTACK, "Esophageal Burn", "Deals notable damage to a target in return for recoil damage dealt to you.", false, false);
    }

    @Override
    public void commit(Actor user, Actor target) {

        int dmg = target.takeDamage((int)(user.getStrength() * 1.5d));
        int recoil_dmg = dmg/3 + random.nextInt(dmg/4);
        System.out.println(user + " used " + getName() + " on " + (target == user ? "itself" : target.getName()) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
        System.out.println(user + " took " + Colorizer.RED + recoil_dmg + Colorizer.RESET + " recoil damage.");
    }
}
