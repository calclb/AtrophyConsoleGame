package me.ubuntoof.events.actors;

import me.ubuntoof.actions.Action;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;

public class ActionCommitEvent extends Event implements Cancellable
{
    private boolean cancelled;
    public final Actor user;
    public final Action action;
    public final Actor target;

    public ActionCommitEvent(Actor user, Action action, Actor target)
    {
        this.user = user;
        this.action = action;
        this.target = target;
    }

    @Override public boolean isCancelled()
    {
        return cancelled;
    }

    @Override public void setCancelled(boolean b)
    {
        cancelled = b;
    }

    @Override public void perform()
    {
        action.commit(user, target);
    }
}
