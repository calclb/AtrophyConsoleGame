package me.ubuntoof.Characters;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Actions.Action.ActionType;
import me.ubuntoof.Handlers.Battle;
import me.ubuntoof.Utils.Colorizer;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Bandit extends Actor {
    Random random = new Random();

    private static Action a1 = new Action(ActionType.ATTACK, "Chip Away", "Reduces target HP by 10% as true damage.", true) {

        @Override
        public void commit(Actor user, Actor target) {
            // display attack notification
            double dmg = user.getMaxHealth() / 10d;
            System.out.println(user + " used " + a1.getName() + " on " + target + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
            target.takeDamage(dmg);
        }
    };

    private static Action a2 = new Action(ActionType.ATTACK, "Lunge", "Has a 25% chance of dealing double damage.", true) {

        @Override
        public void commit(Actor user, Actor target) {
            double randResult = ThreadLocalRandom.current().nextDouble();
            double dmg = Math.sqrt(user.getLevel()) + 2d;
            System.out.println(user + " used " + a2.getName() + " on " + target + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
            target.takeDamage(0.25d > randResult ? dmg : dmg * 2d);
        }
    };

    private static Action[] actions = new Action[]{a1, a2};

    public Bandit(int level) {
        super("Bandit", actions, level);

        setBaseMaxHealth(2 + Math.sqrt(level));
        setBaseHealth(getMaxHealth());
        setBaseSpeed(4 + Math.sqrt(Math.pow(level, 1.4d)));
        setBaseStrength(2 + Math.sqrt(level));
    }

    @Override
    public void onUserTurn() {
        Actor[] potentialTargets = getBattle().getOpposition(this);
        doAction(potentialTargets[random.nextInt(potentialTargets.length)], random.nextInt(actions.length));
    }
}
