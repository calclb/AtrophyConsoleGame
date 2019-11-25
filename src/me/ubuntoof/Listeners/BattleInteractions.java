package me.ubuntoof.Listeners;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Handlers.Battle;
import me.ubuntoof.Modifiers.Ailment;
import me.ubuntoof.Modifiers.StatModifier;

public interface BattleInteractions {

    void onBattleStarted(Battle battle);
    void onUserTurn();
    default void onGlobalTurnStarted() {}
    default void onGlobalTurnEnded() {}
    default void onBattleEnded() {}

    default void onAnyoneHealed() {}
    default void onAnyoneAttacked() {}
}
