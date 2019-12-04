package me.ubuntoof.Characters;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Actions.Action.ActionType;
import me.ubuntoof.Handlers.Battle;
import me.ubuntoof.Modifiers.StatModifier;
import me.ubuntoof.Utils.Colorizer;
import me.ubuntoof.Utils.TextFormatter;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Bandit extends Actor {

    private static Action a1 = new Action(ActionType.ATTACK, "Chip Away", "Reduces target HP by 10% as true damage.", false, true) {

        @Override
        public void commit(Actor user, Actor target) {
            int dmg = target.takeDamage(Math.max(target.getMaxHealth() / 10, 1), true);
            System.out.println(user + " used " + a1.getName() + " on " + (target == user ? "itself" : target.getName()) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
        }
    };

    private static Action a2 = new Action(ActionType.ATTACK, "Lunge", "Has a 25% chance of dealing double damage.", false, true) {

        @Override
        public void commit(Actor user, Actor target) {

            double randResult = random.nextDouble();
            int dmg = target.takeDamage((int)((0.25d > randResult ? 2d : 1d) * Math.sqrt((user.getLevel() + 2d))));
            System.out.println(user + " used " + a2.getName() + " on " + (target == user ? "itself" : target.getName()) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
        }
    };

    private static Action[] actions = new Action[]{a1, a2};

    public Bandit(int level) {
        super("Bandit", actions, level);

        setBaseMaxHealth((int)(2 + Math.sqrt(level)));
        setBaseHealth(getMaxHealth());
        setBaseStrength((int)(2 + Math.sqrt(level)));
        setBaseDefense(level/8);
        setBaseArmor((int)(Math.sqrt(level)));
        setBaseSpeed((int)(4 + Math.sqrt(Math.pow(level, 1.4d))));
    }

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
