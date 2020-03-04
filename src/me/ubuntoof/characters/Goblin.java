package me.ubuntoof.characters;

import me.ubuntoof.actions.Action;
import me.ubuntoof.actions.Action.ActionType;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.Stats;
import me.ubuntoof.utils.Colorizer;

public class Goblin extends Actor
{

    public Goblin(int level)
    {
        super("Goblin", level);
        createActions();
        setBaseMaxHealth(2 + level);
        setBaseHealth(getBaseMaxHealth());
        setBaseStrength((int)(1 + Math.sqrt(2*level)));
        setBaseDefense(level/5);
        setBaseArmor((int)(1 + Math.sqrt(level)));
        setBaseSpeed((int)(2 + Math.sqrt(level)));
    }

    @Override public void onUserTurn()
    {
        Actor[] potentialTargets;
        Action action = getActions().get(random.nextInt(getActions().size()));
        Actor target;

        if(action.isSupportive()) potentialTargets = getBattle().getFriendlies(this);
        else potentialTargets = getBattle().getOpposition(this);

        if(action.supportive) target = this;
        else do { target = potentialTargets[random.nextInt(potentialTargets.length)]; } while(!target.isAlive());

        doAction(action, target);
    }

    private void createActions()
    {
        getActions().add(new Action(ActionType.ATTACK, "Pummel", "Deals damage equal to half the user's level.", false, true)
        {
            @Override public void commit(Actor user, Actor target)
            {
                int dmg = target.takeDamage((int)(user.getLevel() * 0.5d), false);
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
            }
        });

        getActions().add(new Action(ActionType.STATUS, "Guard", "Reduces damage taken by 50% until next turn.", true, false)
        {
            @Override public void commit(Actor user, Actor target)
            {
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", increasing defense by 50% until the next turn.");
                user.addStatModifier(new StatModifier(Stats.DEFENSE, 1.5d, 0));
            }
        });
    }
}
