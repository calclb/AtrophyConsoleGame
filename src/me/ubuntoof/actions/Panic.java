package me.ubuntoof.actions;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.utils.Colorizer;

public class Panic extends Action
{
    public Panic() {
        super(ActionType.OTHER, "Panic", "SOS", false, true, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        String msg = "";
        int targetIndex = random.nextInt(user.getBattle().getLivingCombatants().size());
        target = user.getBattle().getLivingCombatants().get(targetIndex);
        int dmg = target.takeDamage(random.nextInt(user.getLevel() + target.getLevel()));
        msg += (user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
        user.getBattle().println(msg);
    }
}
