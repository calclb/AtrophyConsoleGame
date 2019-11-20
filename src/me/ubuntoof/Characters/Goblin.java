package me.ubuntoof.Characters;

import me.ubuntoof.Action;
import me.ubuntoof.Action.ActionType;

public class Goblin extends Actor {

    private static Action a1, a2;

    private static Action[] actions = new Action[]{a1, a2};

    public Goblin(int level) {

        super(actions, level);

        setMaxHealth(2 + level);
        setHealth(getMaxHealth());
        setStrength(1 + Math.sqrt(2*level));
        setSpeed(2 + Math.sqrt(level));

        a1 = new Action(ActionType.ATTACK, "Pummel", "Deals damage equal to half the user's level.", true) {

            @Override
            public void commit(Actor user, Actor target) {
                // display attack notification
                target.takeDamage(user.getLevel() * 0.5d);
            }
        };

        a2 = new Action(ActionType.ATTACK, "Guard", "Take 50% normal damage until the next turn.", true) {

            @Override
            public void commit(Actor user, Actor target) {
                // TODO add stat modifiers
            }
        };

    }

    public Action[] getActions() { return actions; }
}
