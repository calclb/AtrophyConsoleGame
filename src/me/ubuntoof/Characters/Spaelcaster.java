package me.ubuntoof.Characters;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Actions.Action.ActionType;
import me.ubuntoof.Handlers.Battle;
import me.ubuntoof.Modifiers.Ailment;
import me.ubuntoof.Modifiers.Ailments.Paralysis;
import me.ubuntoof.Modifiers.Ailments.Poison;
import me.ubuntoof.Modifiers.StatModifier;
import me.ubuntoof.Stats;
import me.ubuntoof.Utils.Colorizer;
import me.ubuntoof.Utils.TextFormatter;

import java.util.Random;

public class Spaelcaster extends Actor {

    private static Action a1 = new Action(ActionType.STATUS, "Hex", "Poisons the target.", false, false) {

        @Override public void commit(Actor user, Actor target) {

            Poison poison = new Poison(5);
            System.out.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", afflicting " + poison.name + Colorizer.RESET + " for " + poison.getDurationInTurns() + " turns.");
            target.addAilment(poison);
        }
    };

    private static Action a2 = new Action(ActionType.ATTACK, "Sting", "Deals damage equal to user's speed, and has a chance of Paralyzing the target.", false, true) {

        @Override public void commit(Actor user, Actor target) {

            int dmg = target.takeDamage(user.getSpeed());
            boolean doesParalysisApply = random.nextInt(5) < 2;

            System.out.print(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage"
                    + (doesParalysisApply ? "" : ".\n"));

            if(doesParalysisApply)
            {
                Paralysis paralysis = new Paralysis(3);
                System.out.println(" and inflicting " + paralysis.name + Colorizer.RESET + " for " + paralysis.getDurationInTurns() + " turns.");
                target.addAilment(paralysis);
            }
        }
    };


    private static Action a3 = new Action(ActionType.STATUS, "Rejuvenate", "Heals target health by user's Strength.", true, false) {

        @Override public void commit(Actor user, Actor target)
        {
            int healAmt = user.getStrength();
            target.setBaseHealth(target.getHealth() + healAmt);
            System.out.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", healing "
                    + Colorizer.GREEN + healAmt + Colorizer.RESET + " health.");
        }
    };

    private static Action[] actions = new Action[]{a1, a2, a3};

    public Spaelcaster(int level) {

        super("Spaelcaster", actions, level);
        setBaseMaxHealth(2 + level/2);
        setBaseHealth(getBaseMaxHealth());
        setBaseStrength((int)(2 + Math.sqrt(1.5*level)));
        setBaseDefense((int)(Math.sqrt(level)/4));
        setBaseArmor((int)(1 + Math.sqrt(level)));
        setBaseSpeed((int)(3 + Math.sqrt(level)));
    }

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
