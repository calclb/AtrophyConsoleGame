package me.ubuntoof.characters;

import me.ubuntoof.StatContainer;
import me.ubuntoof.actions.Action;
import me.ubuntoof.actions.Action.ActionType;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.utils.Colorizer;

import java.util.List;

public class Druidimite extends Actor
{
    public Druidimite(int level)
    {
        super("Druidimite", level, new StatContainer(4, 1, 1, 1, 1, 1, 1)); // TODO balance
        createActions();
    }

    private void createActions()
    {
        getSignatureActions().add(new Action(ActionType.ATTACK, "Boink", "Deals 3 true damage.", false, true)
        {
            @Override public void commit(Actor user, Actor target)
            {
                ActorDamageEvent ade = target.takeDamage(3, true);
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage.");
            }
        });
    }

    @Override public void onUserTurn()
    {
        List<Actor> potentialTargets;
        List<Action> actions = getActions();
        Action action = actions.get(random.nextInt(actions.size()));

        if(action.isSupportive()) potentialTargets = getBattle().getFriendlies(this);
        else potentialTargets = getBattle().getOpposition(this);

        Actor target;
        do {target = potentialTargets.get(random.nextInt(potentialTargets.size())); } while(!target.isAlive());

        doAction(action, target);
    }
}
