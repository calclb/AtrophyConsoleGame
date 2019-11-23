package me.ubuntoof.Characters;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Listeners.TurnListener;

public class Player extends Actor implements TurnListener {

    int exp = 0;

    public Player(String name, Action[] actions, int level) {
        super(name, actions, level);
    }

    @Override
    public void onBattleStarted()
    {

    }

    @Override
    public void onUserTurn() {

        promptUser();
    }

    @Override
    public void onGlobalTurnEnded()
    {

    }

    @Override
    public void onBattleEnded()
    {

    }

    private void promptUser()
    {
        System.out.println("");
    }
}
