package me.ubuntoof.Characters;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Actions.Action.ActionType;
import me.ubuntoof.Handlers.Battle;
import me.ubuntoof.Modifiers.StatModifier;
import me.ubuntoof.Stats;
import me.ubuntoof.Utils.Colorizer;

import java.util.Random;

public class Goblin extends Actor {

    private static Action a1 = new Action(ActionType.ATTACK, "Pummel", "Deals damage equal to half the user's level.", true) {

        @Override public void commit(Actor user, Actor target) {

            double dmg = user.getLevel() * 0.5d;
            System.out.println(user + " used " + a1.getName() + " on " + target + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
            target.takeDamage(dmg, false);
        }
    };

    private static Action a2 = new Action(ActionType.ATTACK, "Guard", "Reduces damage taken by 50% until next turn.", true) {

        @Override public void commit(Actor user, Actor target)
        {

            System.out.println(user + " used " + a2.getName() + " on " + target + ".");
            user.getStatModifiers().add(new StatModifier(Stats.DEFENSE, 1.5d, 0));
            // TODO use listeners to reduce damage
        }
    };

    private static Action[] actions = new Action[]{a1, a2};
    Random random = new Random();

    public Goblin(int level) {

        super("Goblin", actions, level);

        setBaseMaxHealth(2 + level);
        setBaseHealth(getBaseMaxHealth());
        setBaseStrength(1 + Math.sqrt(2*level));
        setBaseSpeed(2 + Math.sqrt(level));
    }

    // Interface overrides
    @Override
    public void onUserTurn() {
        Actor[] potentialTargets = getBattle().getOpposition(this);
        doAction(potentialTargets[random.nextInt(potentialTargets.length)], random.nextInt(actions.length-1));
    }
}
