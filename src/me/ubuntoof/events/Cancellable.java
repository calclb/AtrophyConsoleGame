package me.ubuntoof.events;

public interface Cancellable
{
    boolean isDisallowed();
    void disallow();
}
