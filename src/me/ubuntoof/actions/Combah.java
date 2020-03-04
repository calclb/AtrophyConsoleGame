package me.ubuntoof.actions;

import me.ubuntoof.Stats;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.utils.Colorizer;

public class Combah extends Action
{
    public Combah()
    {
        super(ActionType.ATTACK, "Combah", "Randomly hits 1-3 times using half of the user's " + Stats.STRENGTH.getName() + ".", false, true);
    }

    @Override public void commit(Actor user, Actor target)
    {
        int hits = random.nextInt(3) + 1;
        int dmg;
        String msg = "";

        msg += user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", hitting " + hits + " time" + (hits == 1 ? "" : "s") + " for ";

        for(int i = 1; i <= hits; i++)
        {
            dmg = target.takeDamage(Math.max(((user.getStrength() + random.nextInt(user.getStrength()/4) - user.getStrength()/4) / 2), 1));

            msg += Colorizer.RED + dmg + Colorizer.RESET;
            if(hits > 2 && hits - i > 1) msg += ", ";
            if(hits > 1 && hits - i == 1) msg += " and ";
        }

        msg += " damage.";

        user.getBattle().println(msg);
    }
}
