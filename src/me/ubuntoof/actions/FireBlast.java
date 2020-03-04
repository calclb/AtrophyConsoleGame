package me.ubuntoof.actions;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.utils.Colorizer;

public class FireBlast extends Action
{
    public FireBlast()
    {
        super(ActionType.ATTACK, "Fire Blast", "Attacks all enemies with a semi-powerful wave of heat.", false, false, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        String msg = "";

        for(Actor a : user.getBattle().getLivingCombatants()) if (a != user)
        {
            int dmg = a.takeDamage(Math.max(user.getStrength() / 3 + a.getHealth() / 8 - a.getSpeed() / 2, 0));
            msg += (user + " used " + getName() + " on " + a + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.\n");
        }

        user.getBattle().print(msg);
    }
}
