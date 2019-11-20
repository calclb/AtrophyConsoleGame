package me.ubuntoof;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Utils.TextFormatter;

public abstract class Action {

    private ActionType type;
    private String name;
    private String description;
    private boolean usesContact;

    protected Action(ActionType type, String name, String description, boolean usesContact)
    {
        this.type = type;
        this.name = name;
        this.description = description;
        this.usesContact = usesContact;
    }

    public ActionType getType() { return type; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    boolean usesContact() { return usesContact; }

    public abstract void commit(Actor user, Actor target);

    public String toString() { return TextFormatter.formatAction(type, name, description); }

    public enum ActionType
    {
        ATTACK("Attack"), STATUS("Status"), OTHER("Other");

        private String val;

        ActionType(String assignedValue) { val = assignedValue; }

        @Override public String toString() { return val; }

    }
}
