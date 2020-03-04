package me.ubuntoof.characters;

import me.ubuntoof.actions.Action;
import me.ubuntoof.actions.Action.ActionType;
import me.ubuntoof.utils.Colorizer;

import java.util.List;

public class Druidimite extends Actor
{
    public Druidimite(int level)
    {
        super("Druidimite", level);
        createActions();

        setBaseMaxHealth((int)(1 + Math.sqrt(level)));
        setBaseHealth(getMaxHealth());
        setBaseStrength((int)(1 + Math.sqrt(level)));
        setBaseDefense(level/10);
        setBaseArmor((int)(Math.sqrt(level)));
        setBaseSpeed((int)(2 + Math.sqrt(Math.pow(level, 1.2d))));
    }

    private void createActions()
    {
        getActions().add(new Action(ActionType.ATTACK, "Boink", "Deals 3 true damage.", false, true)
        {
            @Override public void commit(Actor user, Actor target)
            {
                int dmg = target.takeDamage(3, true);
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
            }
        });
    }

    @Override public void onUserTurn()
    {
        Actor[] potentialTargets;
        List<Action> actions = getActions();
        Action action = actions.get(random.nextInt(actions.size()));

        if(action.isSupportive()) potentialTargets = getBattle().getFriendlies(this);
        else potentialTargets = getBattle().getOpposition(this);

        Actor target;
        do {target = potentialTargets[random.nextInt(potentialTargets.length)]; } while(!target.isAlive());

        doAction(action, target);
    }
}
