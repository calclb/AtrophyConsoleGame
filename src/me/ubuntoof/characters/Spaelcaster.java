package me.ubuntoof.characters;

import me.ubuntoof.StatContainer;
import me.ubuntoof.actions.Action;
import me.ubuntoof.actions.Action.ActionType;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.modifiers.ailments.Paralysis;
import me.ubuntoof.modifiers.ailments.Poison;
import me.ubuntoof.utils.Colorizer;

import java.util.List;

public class Spaelcaster extends Actor
{
    public Spaelcaster(int level)
    {
        super("Spaelcaster", level, new StatContainer(15, 2, 8, 3, 4, 5, 4));
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
        getSignatureActions().add(new Action(ActionType.STATUS, "Hex", "Poisons the target.", false, false)
        {
            @Override public void commit(Actor user, Actor target)
            {
                Poison poison = new Poison(5);
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", afflicting " + poison.name + Colorizer.RESET + " for " + poison.getDurationInTurns() + " turns.");
                target.addAilment(poison);
            }
        });

        getSignatureActions().add(new Action(ActionType.ATTACK, "Sting", "Deals damage equal to user's speed, and has a chance of Paralyzing the target.", false, true)
        {
            @Override public void commit(Actor user, Actor target)
            {
                ActorDamageEvent ade = target.takeDamage(user.getAgility());
                boolean doesParalysisApply = random.nextInt(5) < 2;

                getBattle().print(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage"
                        + (doesParalysisApply ? "" : ".\n"));

                if(doesParalysisApply)
                {
                    Paralysis paralysis = new Paralysis(3);
                    getBattle().println(" and inflicting " + paralysis.name + Colorizer.RESET + " for " + paralysis.getDurationInTurns() + " turns.");
                    target.addAilment(paralysis);
                }
            }
        });

        getSignatureActions().add(new Action(ActionType.STATUS, "Rejuvenate", "Heals target health by user's Strength.", true, false)
        {
            @Override public void commit(Actor user, Actor target)
            {
                int healAmt = user.getStrength();
                target.setHealth(Math.min(target.getHealth() + healAmt, target.getStamina()));
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", healing "
                        + Colorizer.GREEN + healAmt + Colorizer.RESET + " health.");
            }
        });
    }
}
