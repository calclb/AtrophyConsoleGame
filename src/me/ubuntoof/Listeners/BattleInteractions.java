package me.ubuntoof.Listeners;

import me.ubuntoof.Handlers.Battle;

public interface BattleInteractions {

    void onBattleStarted(Battle battle);
    void onUserTurn();
    default void onGlobalTurnStarted() {}
    default void onGlobalTurnEnded() {}
    default void onBattleEnded() {}

    default void onAnyoneHealed() {}
    default void onAnyoneAttacked() {}
}
