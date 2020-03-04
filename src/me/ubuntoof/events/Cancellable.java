package me.ubuntoof.events;

public interface Cancellable
{
    boolean isCancelled();
    void setCancelled(boolean b);
}
