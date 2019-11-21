package me.ubuntoof.Characters;

import me.ubuntoof.Action;
import me.ubuntoof.Action.ActionType;

import java.util.concurrent.ThreadLocalRandom;

public class Bandit extends Actor {

    private static Action a1, a2;

    private static Action[] actions = new Action[]{a1, a2};

    public Bandit(int level) {

        super(actions, level);

        setMaxHealth(2 + Math.sqrt(level));
        setHealth(getMaxHealth());
        setSpeed(4 + Math.sqrt(Math.pow(level, 1.4d)));
        setStrength(2 + Math.sqrt(level));

        a1 = new Action(ActionType.ATTACK, "Chip Away", "Reduces target HP by 10% as true damage.", true) {

            @Override
            public void commit(Actor user, Actor target) {
                // display attack notification
                double dmg = target.getMaxHealth() / 10d;
                target.takeDamage(dmg);
            }
        };

        a2 = new Action(ActionType.ATTACK, "Lunge", "Has a 25% chance of dealing double damage.", true) {

            @Override
            public void commit(Actor user, Actor target) {

                double randResult = ThreadLocalRandom.current().nextDouble();
                double dmg = Math.sqrt(user.getLevel()) + 2d;
                target.takeDamage(0.25d > randResult ? dmg : dmg * 2d);
            }
        };

    }

    public Action[] getActions() { return actions; }
}
