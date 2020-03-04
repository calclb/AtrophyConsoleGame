package me.ubuntoof.characters;

import me.ubuntoof.actions.Action;
import me.ubuntoof.actions.Action.ActionType;
import me.ubuntoof.handlers.Battle;
import me.ubuntoof.modifiers.Ailment;
import me.ubuntoof.utils.Colorizer;

import java.util.List;

public class Druid extends Actor
{

    public Druid(int level) {

        super("Druid", level);
        createActions();
        setBaseMaxHealth(2 + level/2);
        setBaseHealth(getBaseMaxHealth());
        setBaseStrength((int)(2 + Math.sqrt(1.5*level)));
        setBaseDefense((int)(Math.sqrt(level)/4));
        setBaseArmor((int)(1 + Math.sqrt(level)));
        setBaseSpeed((int)(3 + Math.sqrt(level)));
    }

    /* TODO Possible decision AI class? */
    @Override
    public void onUserTurn() {

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
        getActions().add(new Action(ActionType.STATUS, "Second Wind", "Heals the target by 5% of their HP over 5 turns.", true, false)
        {
            @Override public void commit(Actor user, Actor target)
            {
                Ailment secondWindAilment = new Ailment("", getName(), getDescription(), 5)
                {
                    @Override public void applyEffects(Actor target)
                    {
                        int healAmt = (int)(Math.max(target.getMaxHealth()/20d, 1));
                        target.setBaseHealth(target.getHealth() + healAmt);
                        target.getBattle().println(Colorizer.RESET + target.getName() + " was healed by "
                                + name + " for " + Colorizer.GREEN + healAmt + Colorizer.RESET + " health.");
                    }

                    @Override public boolean equals(Object o) { return false; }
                };

                getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", applying " + secondWindAilment.name + " - " + secondWindAilment.description);

                user.addAilment(secondWindAilment);
            }
        });

        getActions().add(new Action(ActionType.STATUS, "Summon", "Has a 50% chance to form two ally Druidimites.", true, false)
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

        getActions().add(new Action(ActionType.STATUS, "Rejuvenate", "Heals target health by user's Strength.", true, false)
        {
            @Override public void commit(Actor user, Actor target)
            {
                target.setBaseHealth(target.getHealth() + user.getStrength());
                user.getBattle().println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", healing "
                        + Colorizer.GREEN + user.getLevel()/3 + Colorizer.RESET + " health.");
            }
        });
    }
}
