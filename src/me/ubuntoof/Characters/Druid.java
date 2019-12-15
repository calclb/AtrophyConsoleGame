package me.ubuntoof.Characters;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Actions.Action.ActionType;
import me.ubuntoof.Handlers.Battle;
import me.ubuntoof.Modifiers.Ailment;
import me.ubuntoof.Utils.Colorizer;

public class Druid extends Actor {

    private static Action a1 = new Action(ActionType.STATUS, "Second Wind", "Heals the target by 5% of their HP over 5 turns.", true, false) {

        @Override public void commit(Actor user, Actor target) {

            Ailment secondWindAilment = new Ailment("", getName(), getDescription(), 5) {
                @Override
                public void applyEffects(Actor target) {
                    int healAmt = (int)(Math.max(target.getMaxHealth()/20d, 1));
                    target.setBaseHealth(target.getHealth() + healAmt);
                    System.out.println(Colorizer.RESET + target.getAndFormatThisCombatantIndex() + " " + target.getName() + " was healed by "
                            + name + " for " + Colorizer.GREEN + healAmt + Colorizer.RESET + " health.");
                }

                @Override
                public boolean equals(Object o) {
                    return false;
                }
            };


            System.out.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", applying " + secondWindAilment.name + " - " + secondWindAilment.description);

            target.getAilments().add(secondWindAilment);
        }
    };

    private static Action a2 = new Action(ActionType.STATUS, "Summon", "Has a 50% chance to form two ally Druidimites.", true, false) {

        @Override public void commit(Actor user, Actor target) {

            boolean occurs = random.nextInt(2) < 0;

            System.out.println(user + " used " + getName() + ", "
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
    };


    private static Action a3 = new Action(ActionType.STATUS, "Rejuvenate", "Heals target health by user's Strength.", true, false) {

        @Override public void commit(Actor user, Actor target)
        {
            target.setBaseHealth(target.getHealth() + user.getStrength());
            System.out.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", healing "
                    + Colorizer.GREEN + user.getLevel()/3 + Colorizer.RESET + " health.");
        }
    };

    private static Action[] actions = new Action[]{a1, a2, a3};

    public Druid(int level) {

        super("Druid", actions, level);
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
        Action action = getActions()[random.nextInt(actions.length)];

        if(action.isSupportive()) potentialTargets = getBattle().getFriendlies(this);
        else potentialTargets = getBattle().getOpposition(this);

        Actor target;
        do {target = potentialTargets[random.nextInt(potentialTargets.length)]; } while(!target.isAlive());

        doAction(action, target);
    }
}
