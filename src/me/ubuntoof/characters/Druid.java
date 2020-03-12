package me.ubuntoof.characters;

import me.ubuntoof.StatContainer;
import me.ubuntoof.actions.Action;
import me.ubuntoof.actions.Action.ActionType;
import me.ubuntoof.handlers.Battle;
import me.ubuntoof.modifiers.Ailment;
import me.ubuntoof.utils.Colorizer;

import java.util.List;

public class Druid extends Actor
{

    public Druid(int level) {

        super("Druid", level, new StatContainer(15, 2,7, 3, 5, 3, 3)); // TODO balance
        createActions();
    }

    /* TODO Possible decision AI class? */
    @Override
    public void onUserTurn() {

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
        getSignatureActions().add(new Action(ActionType.STATUS, "Second Wind", "Heals the target by 5% of their HP over 5 turns.", true, false)
        {
            @Override public void commit(Actor user, Actor target)
            {
                Ailment secondWindAilment = new Ailment("", getName(), getDescription(), 5)
                {
                    @Override public void applyEffects(Actor target)
                    {
                        int healAmt = (int)(Math.max(target.getStamina()/20d, 1));
                        target.setHealth(Math.min(target.getStamina(), target.getHealth() + healAmt));
                        target.getBattle().println(Colorizer.RESET + target.getName() + " was healed by "
                                + name + " for " + Colorizer.GREEN + healAmt + Colorizer.RESET + " health.");
                    }

                    @Override public boolean equals(Object o) { return false; }
                };

                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", applying " + secondWindAilment.name + " - " + secondWindAilment.description);

                user.addAilment(secondWindAilment);
            }
        });

        getSignatureActions().add(new Action(ActionType.STATUS, "Summon", "Has a 50% chance to form two ally Druidimites.", true, false)
        {
            @Override public void commit(Actor user, Actor target)
            {
                boolean occurs = random.nextInt(2) > 0;

                user.getBattle().println(user + " used " + getName() + ", "
                        + (occurs ? "creating two ally Druidimites." : "but it failed."));

                if(occurs)
                {
                    Battle battle = user.getBattle();
                    for(int i = 0; i < 2; i++)
                    {
                        Druidimite druidimite = new Druidimite(user.getLevel()/2);
                        battle.scheduleAddActorToBattle(druidimite, battle.getTeamOf(user));
                    }
                }
            }
        });

        getSignatureActions().add(new Action(ActionType.STATUS, "Rejuvenate", "Heals target health by user's Strength.", true, false)
        {
            @Override public void commit(Actor user, Actor target)
            {
                target.setHealth(Math.min(target.getStamina(), target.getHealth() + user.getStrength()));
                user.getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", healing "
                        + Colorizer.GREEN + user.getLevel()/3 + Colorizer.RESET + " health.");
            }
        });
    }
}
