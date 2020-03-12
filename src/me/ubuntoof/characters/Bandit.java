package me.ubuntoof.characters;

import me.ubuntoof.StatContainer;
import me.ubuntoof.actions.Action;
import me.ubuntoof.actions.Action.ActionType;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.utils.Colorizer;

import java.util.List;

public class Bandit extends Actor
{
    public Bandit(int level)
    {
        super("Bandit", level, new StatContainer(10, 4, 4, 6, 6, 7, 8));
        createActions();
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

    private void createActions()
    {
        getSignatureActions().add(new Action(ActionType.ATTACK, "Chip Away", "Reduces target HP by 10% as true damage.", false, true)
        {
            @Override public void commit(Actor user, Actor target)
            {
                ActorDamageEvent ade = target.takeDamage(Math.max(target.getStamina() / 10, 1), true);
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage.");
            }
        });

        getSignatureActions().add(new Action(ActionType.ATTACK, "Lunge", "Has a 25% chance of dealing double damage.", false, true)
        {
            @Override public void commit(Actor user, Actor target)
            {
                double randResult = random.nextDouble();
                ActorDamageEvent ade = target.takeDamage((int)((0.25d > randResult ? 2d : 1d) * Math.sqrt((user.getLevel() + 2d))));
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage.");
            }
        });
    }
}
