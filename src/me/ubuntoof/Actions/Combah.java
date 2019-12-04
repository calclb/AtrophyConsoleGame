package me.ubuntoof.Actions;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Stats;
import me.ubuntoof.Utils.Colorizer;

import java.util.Random;

public class Combah extends Action {

    public Combah() {
        super(ActionType.ATTACK, "Combah", "Randomly hits 1-3 times using half of the user's " + Stats.STRENGTH.getName() + ".", false, true);
    }

    @Override
    public void commit(Actor user, Actor target) {
        int hits = random.nextInt(3) + 1;
        int dmg;

        System.out.print(user + " used " + getName() + " on " + (target == user ? "itself" : target.getName()) + ", hitting " + hits + " time" + (hits == 1 ? "" : "s") + " for ");

        // TODO fix
        for(int i = 1; i <= hits; i++)
        {
            dmg = target.takeDamage(Math.max(((user.getStrength() + random.nextInt(user.getStrength()/4) - user.getStrength()/4) / 2), 1));

            System.out.print(Colorizer.RED + dmg + Colorizer.RESET);
            if(hits > 2 && hits - i > 1) System.out.print(", ");
            if(hits > 1 && hits - i == 1) System.out.print(" and ");
        }

        System.out.print(" damage.");
        System.out.println();
    }
}
