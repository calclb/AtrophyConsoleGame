package me.ubuntoof.Actions;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Utils.TextFormatter;

import java.util.Random;

public abstract class Action {

    private ActionType type;
    private String name;
    private String description;
    private boolean supportive;
    private boolean usesContact;
    protected final static Random random = new Random();

    protected Action(ActionType type, String name, String description, boolean supportive, boolean usesContact)
    {
        this.type = type;
        this.name = name;
        this.description = description;
        this.supportive = supportive;
        this.usesContact = usesContact;
    }

    public ActionType getType() { return type; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isSupportive() { return supportive; }
    public boolean usesContact() { return usesContact; }

    /* has to have 'user' parameter so actions which have dedicated classes can reference their properties (anonymous classes wouldn't require it
       since they have the class reference itself) */
    public abstract void commit(Actor user, Actor target);

    public String toString() { return TextFormatter.formatAction(type, name, description); }

    public enum ActionType
    {
        ATTACK("\uD83D\uDCA5"), STATUS("☯️"), OTHER("❓");

        private String val;

        ActionType(String assignedValue)
        {
            val = assignedValue;
        }

        @Override public String toString() { return val; }

    }
}
