package me.ubuntoof.Listeners;

import me.ubuntoof.Characters.Actor;

public interface TurnListener {

    default void onBattleStarted() {}
    default void onUserTurn() {}
    default void onGlobalTurnEnded() {}
    default void onBattleEnded() {}
}
