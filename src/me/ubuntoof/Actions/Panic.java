package me.ubuntoof.Actions;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Utils.Colorizer;

public class Panic extends Action {
    public Panic() {
        super(ActionType.OTHER, "Panic", "SOS", false, true);
    }

    @Override
    public void commit(Actor user, Actor target)
    {
        int targetIndex = random.nextInt(user.getBattle().getCombatants().length);
        target = user.getBattle().getCombatants()[targetIndex];
        int dmg = target.takeDamage(random.nextInt(user.getLevel() + target.getLevel()));
        System.out.println(user + " used " + getName() + " on " + (target == user ? "itself" : target.getName()) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");

    }
}
