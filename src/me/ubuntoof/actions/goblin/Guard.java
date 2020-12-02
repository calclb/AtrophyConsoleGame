package me.ubuntoof.actions.goblin;

import me.ubuntoof.actions.Action;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.passives.Condition;

import java.text.NumberFormat;

public class Guard extends Action
{
    private final double multiplier = .5;
    private final NumberFormat nf = NumberFormat.getPercentInstance();

    public Guard()
    {
        super(ActionType.STATUS, "Guard", "Reduces damage taken by 50% until next turn.", true, false, false);

    }


    @Override public void commit(Actor user, Actor target)
    {
        user.battle.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", reducing damage by " + nf.format(1 - multiplier) + " until the next turn.");

        user.addCondition(new Condition(getName(), getDescription(), 1)
        {
            /**
             * @param owner The Actor to run with the Condition
             * @param e     The event to analyze
             */
            @Override public void on(Actor owner, Event e)
            {
                if(e instanceof ActorDamageEvent)
                {
                    ActorDamageEvent ade = (ActorDamageEvent) e;
                    if(ade.getActor() == owner) ade.setDamage((int) (ade.getDamage()*multiplier));
                }
            }
        });
    }
}
