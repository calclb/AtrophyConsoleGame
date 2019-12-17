package me.ubuntoof.Listeners;

public interface ActorInteractions
{
    void onActorTurn();
    default void onActorAttack() {}
}
