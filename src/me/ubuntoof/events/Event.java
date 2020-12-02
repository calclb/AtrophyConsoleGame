package me.ubuntoof.events;

public abstract class Event
{
    /**
     * Called after none of the observers disallow a Cancellable event, or immediately if the Event isn't Cancellable
     */
    public void perform() {}
}
