package me.ubuntoof.characters;

import me.ubuntoof.actions.Action;
import me.ubuntoof.actions.Action.ActionType;
import me.ubuntoof.utils.Colorizer;

import java.util.List;

public class Bandit extends Actor
{
    public Bandit(int level)
    {
        super("Bandit", level);
        createActions();

        setBaseMaxHealth((int)(2 + Math.sqrt(level)));
        setBaseHealth(getMaxHealth());
        setBaseStrength((int)(2 + Math.sqrt(level)));
        setBaseDefense(level/8);
        setBaseArmor((int)(Math.sqrt(level)));
        setBaseSpeed((int)(4 + Math.sqrt(Math.pow(level, 1.4d))));
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

    private void createActions()
    {
        getActions().add(new Action(ActionType.ATTACK, "Chip Away", "Reduces target HP by 10% as true damage.", false, true)
        {
            @Override public void commit(Actor user, Actor target)
            {
                int dmg = target.takeDamage(Math.max(target.getMaxHealth() / 10, 1), true);
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
            }
        });

        getActions().add(new Action(ActionType.ATTACK, "Lunge", "Has a 25% chance of dealing double damage.", false, true)
        {
            @Override public void commit(Actor user, Actor target)
            {
                double randResult = random.nextDouble();
                int dmg = target.takeDamage((int)((0.25d > randResult ? 2d : 1d) * Math.sqrt((user.getLevel() + 2d))));
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
            }
        });
    }
}
