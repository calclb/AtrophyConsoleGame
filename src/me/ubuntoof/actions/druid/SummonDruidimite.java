package me.ubuntoof.actions.druid;

import me.ubuntoof.actions.Action;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.entities.Druidimite;
import me.ubuntoof.handlers.Battle;

public class SummonDruidimite extends Action
{
    private boolean canUseAction = true;

    public SummonDruidimite()
    {
        super(ActionType.STATUS, "Summon", "Has a 50% chance to form two ally Druidimites.", true, false);
    }

    @Override public void commit(Actor user, Actor target)
    {
        boolean occurs = random.nextInt(2) > 0 && canUseAction;
        user.battle.println(user + " used " + getName() + ", "
                + (occurs ? "creating two ally Druidimites." : "but it failed."));

        if (occurs)
        {
            canUseAction = false;
            Battle b = user.battle;
            for (int i = 0; i < 2; i++)
            {
                Druidimite druidimite = new Druidimite(user.level / 2);
                b.scheduleAddEntityToBattle(druidimite, user.getTeam());
            }
        }
    }
}
