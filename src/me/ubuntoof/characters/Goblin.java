package me.ubuntoof.characters;

import me.ubuntoof.StatContainer;
import me.ubuntoof.Stats;
import me.ubuntoof.actions.Action;
import me.ubuntoof.actions.Action.ActionType;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.events.state.GlobalTurnEndEvent;
import me.ubuntoof.events.statmodifiers.StatModifierAddEvent;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.passives.Passive;
import me.ubuntoof.passives.triggers.Trigger;
import me.ubuntoof.utils.Colorizer;

import java.util.List;

public class Goblin extends Actor
{

    public Goblin(int level)
    {
        super("Goblin", level, new StatContainer(20, 5, 2, 2, 3, 1, 3)); // TODO balance
        createActions();
    }

    @Override public void onUserTurn()
    {
        List<Actor> potentialTargets;
        Action action = getActions().get(random.nextInt(getActions().size()));
        Actor target;

        if(action.isSupportive()) potentialTargets = getBattle().getFriendlies(this);
        else potentialTargets = getBattle().getOpposition(this);

        if(action.supportive) target = this;
        else do { target = potentialTargets.get(random.nextInt(potentialTargets.size())); } while(!target.isAlive());

        doAction(action, target);
    }

    private void createActions()
    {
        getSignatureActions().add(new Action(ActionType.ATTACK, "Pummel", "Deals damage equal to half the user's level.", false, true)
        {
            @Override public void commit(Actor user, Actor target)
            {
                ActorDamageEvent ade = target.takeDamage((int)(user.getLevel() * 0.5d), false);
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + ade + Colorizer.RESET + " damage.");
            }
        });

        getSignatureActions().add(new Action(ActionType.STATUS, "Guard", "Reduces damage taken by 50% until next turn.", true, false)
        {
            @Override public void commit(Actor user, Actor target)
            {
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", increasing defense by 80% until the next turn.");

                addPassive(new Trigger()
                {
                    @Override public void onEvent(Event e)
                    {
                        super.onEvent(e);
                        if(e instanceof ActorDamageEvent)
                        {
                            ActorDamageEvent ade = (ActorDamageEvent) e;
                            if(ade.getActor() == owner) ade.setDamage((int)(ade.getDamage()/(3d/4)));
                        }
                        if(e instanceof GlobalTurnEndEvent) kill();
                    }
                });
            }
        });

        getSignatureActions().add(new Action(ActionType.STATUS, "Intimidate", "Reduces Strength of target by 25% for 2 turns.", false, false)
        {

            @Override public void commit(Actor user, Actor target)
            {
                StatModifier sm = new StatModifier(Stats.STRENGTH, 0.75d, 2);
                getBattle().battleInteractionsHandler.registerEvent(new StatModifierAddEvent(sm, target));
                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) +
                        ", applying " + sm + Colorizer.RESET + " to " + target + " for " + sm.getDurationInTurns() + " turns.");
            }
        });
    }
}
