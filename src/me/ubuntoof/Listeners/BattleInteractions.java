package me.ubuntoof.Listeners;

import me.ubuntoof.Handlers.Battle;

public interface BattleInteractions {

    void onBattleStarted(Battle battle);
    void onActorTurn();
    default void onGlobalTurnStarted() {}
    default void onGlobalTurnEnded() {}
    default void onBattleEnded() {}

    void onTurnChanged();

    default void onAnyoneHealed() {}
    default void onAnyoneAttacked() {}
}
