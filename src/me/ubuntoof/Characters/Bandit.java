package me.ubuntoof.Characters;

import me.ubuntoof.Action;

public class Bandit extends Actor {

    private static Action a1 = new Action(Action.ActionType.ATTACK, "Bop", "Deal 1 damage.");
    private static Action a2 = new Action(Action.ActionType.STATUS, "Blipp", "Raises damage dealt by 1.");

    private static Action[] actions = new Action[]{a1, a2};


    public Bandit(int level) {
        super(actions, level);
    }

    @Override
    public void doAction(int actionIndex, Actor target) {
    }


}
