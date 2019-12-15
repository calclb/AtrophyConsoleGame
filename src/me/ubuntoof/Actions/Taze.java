package me.ubuntoof.Actions;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Modifiers.Ailments.Paralysis;
import me.ubuntoof.Utils.Colorizer;

public class Taze extends Action {
    public Taze() {
        super(ActionType.ATTACK, "Taze", "Deals low damage, but guarantees Paralysis infliction.", false, true);
    }

    @Override
    public void commit(Actor user, Actor target) {
        int dmg = target.takeDamage(user.getStrength()/3);

        Paralysis paralysis = new Paralysis(1);

        System.out.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET +
                " damage and inflicting " + paralysis.name + Colorizer.RESET + " for " + paralysis.getDurationInTurns() + (paralysis.getDurationInTurns() == 1 ? " turn." : " turns."));

        target.getAilments().add(paralysis);
    }
}
