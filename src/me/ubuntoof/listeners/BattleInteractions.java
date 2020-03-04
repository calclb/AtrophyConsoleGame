package me.ubuntoof.listeners;

import me.ubuntoof.events.Event;

public interface BattleInteractions
{
    default void notifyEvent(Event e) {}
}
