package me.ubuntoof.Characters;

import me.ubuntoof.Action;

public class Player extends Actor {

    int exp;
    Action[] actions;

    public Player(Action[] actions, int level) {
        super(actions, level);
    }

    public Action getAction(int index) { return actions[index]; }

}
