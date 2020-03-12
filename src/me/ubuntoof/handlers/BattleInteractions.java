package me.ubuntoof.handlers;

import me.ubuntoof.events.Event;

public interface BattleInteractions
{
    default void onEvent(Event e) {}
}
