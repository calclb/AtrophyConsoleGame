package me.ubuntoof.Characters;

import me.ubuntoof.Action;
import me.ubuntoof.Action.ActionType;

public class Goblin extends Actor {

    private static Action a1, a2;

    private static Action[] actions = new Action[]{a1, a2};

    public Goblin(int level) {

        super(actions, level);

        a1 = new Action(ActionType.ATTACK, "Pummel", "Deals damage equal to the user's level.", true) {

            @Override
            public void commit(Actor user, Actor target) {
                target.takeDamage(user.getLevel());
            }
        };

        a2 = new Action(ActionType.ATTACK, "Guard", "Take 60% normal damage.", true) {

            @Override
            public void commit(Actor user, Actor target) {
                // TODO add stat modifiers
                target.takeDamage(user.getLevel());
            }
        };

    }

    public Action[] getActions() { return actions; }
}
