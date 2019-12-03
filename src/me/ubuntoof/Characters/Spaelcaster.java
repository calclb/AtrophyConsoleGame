package me.ubuntoof.Characters;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Actions.Action.ActionType;
import me.ubuntoof.Handlers.Battle;
import me.ubuntoof.Modifiers.Ailment;
import me.ubuntoof.Modifiers.Ailments.Poison;
import me.ubuntoof.Modifiers.StatModifier;
import me.ubuntoof.Stats;
import me.ubuntoof.Utils.Colorizer;
import me.ubuntoof.Utils.TextFormatter;

import java.util.Random;

public class Spaelcaster extends Actor {

    private static Action a1 = new Action(ActionType.STATUS, "Hex", "Poisons the target.", false, false) {

        @Override public void commit(Actor user, Actor target) {

            Poison poison = new Poison(3);
            System.out.println(user + " used " + getName() + " on " + (target == user ? "itself" : target.getName()) + ", afflicting " + poison.getName() + " for " + poison.getDurationInTurns() + " turns.");
            target.getAilments().add(poison);
        }
    };

    private static Action a2 = new Action(ActionType.STATUS, "Rejuvenate", "Heals target health by user's Strength.", true, false) {

        @Override public void commit(Actor user, Actor target)
        {
            target.setBaseHealth(target.getHealth() + user.getStrength());
            System.out.println(user + " used " + a2.getName() + " on " + (target == user ? "itself" : target.getName()) + ", healing "
                    + Colorizer.GREEN + user.getLevel() + Colorizer.RESET + " health.");
            // TODO use listeners to reduce damage
        }
    };

    private static Action[] actions = new Action[]{a1, a2};
    private Random random = new Random();

    public Spaelcaster(int level) {

        super("Spaelcaster", actions, level);
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

        Actor target = potentialTargets[random.nextInt(potentialTargets.length)];

        doAction(action, target);
    }
}
