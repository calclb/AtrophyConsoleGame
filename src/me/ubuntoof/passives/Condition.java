package me.ubuntoof.passives;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;
import me.ubuntoof.modifiers.Decrementable;

public abstract class Condition extends Decrementable
{
    public final String name;
    public final String description;

    public Condition(String n, String desc)
    {
        name = n;
        description = desc;
        setPermanent();
    }

    public Condition(String n, String desc, int turns)
    {
        name = n;
        description = desc;
        setDurationInTurns(turns);
    }

    @Override public String toString()
    {
        return name + ": " + description;
    }

    /**
     * @param owner The Actor to run with the Condition
     * @param e The event to analyze
     */
    public abstract void on(Actor owner, Event e);
}
