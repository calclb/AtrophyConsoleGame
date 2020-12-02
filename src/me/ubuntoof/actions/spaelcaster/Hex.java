package me.ubuntoof.actions.spaelcaster;

import me.ubuntoof.actions.Action;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.modifiers.ailments.Poison;
import me.ubuntoof.utils.Colorizer;

public class Hex extends Action
{
    private static final int turns = 2;
    public Hex()
    {
        super(ActionType.STATUS, "Hex", "Poisons the target.", false, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        Poison poison = new Poison(turns);
        user.battle.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", afflicting " + poison.name + Colorizer.RESET + " for " + turns + " turns.");
        target.addAilment(poison);
    }
}
