package me.ubuntoof.Characters;

import me.ubuntoof.Action;

public class Goblin extends Actor {

    private static Action a1 = new Action(Action.ActionType.ATTACK, "Bop", "Deal 1 damage.");
    private static Action a2 = new Action(Action.ActionType.ATTACK, "Crush", "Deal 3 damage.");

    private static Action[] actions = new Action[]{a1, a2};

    public Goblin(int level) {

        super(actions, level);
    }

    @Override
    public void doAction(int actionIndex, Actor target) {
        super.doAction(actionIndex, target);
    }
}
