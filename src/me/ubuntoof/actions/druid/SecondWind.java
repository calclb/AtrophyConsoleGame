package me.ubuntoof.actions.druid;

import me.ubuntoof.actions.Action;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.ailments.AilmentTriggerEvent;
import me.ubuntoof.events.state.GlobalTurnEndEvent;
import me.ubuntoof.modifiers.Ailment;
import me.ubuntoof.utils.Colorizer;

public class SecondWind extends Action
{
    private static final int turns = 5;

    public SecondWind()
    {
        super(ActionType.STATUS, "Second Wind", "Heals the target by 5% of their HP over " + turns + " turns.", true, false, true);
    }

    @Override public void commit(Actor user, Actor target)
    {
        Ailment secondWindAilment = new Ailment("", getName(), getDescription())
        {
            @Override public void on(Actor owner, Event e)
            {
                if(e instanceof GlobalTurnEndEvent) applyEffects(owner);
            }

            @Override public void applyEffects(Actor target)
            {
                int healAmt = (int)(Math.max(target.getStamina()/20d, 1));
                target.setHealth(Math.min(target.getStamina(), target.getHealth() + healAmt));
                target.battle.println(Colorizer.RESET + target.name + " was healed by "
                        + name + " for " + Colorizer.GREEN + healAmt + Colorizer.RESET + " health.");
                user.addActionCooldown(SecondWind.this, 3);
            }

            @Override public boolean equals(Object o) { return false; }
        };

        secondWindAilment.setDurationInTurns(5);

        user.battle.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", applying " + secondWindAilment.name + " - " + secondWindAilment.description);

        user.addAilment(secondWindAilment);
    }
}
