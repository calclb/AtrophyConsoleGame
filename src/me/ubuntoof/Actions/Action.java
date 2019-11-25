package me.ubuntoof.Actions;

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
    public boolean usesContact() { return usesContact; }

    /* has to have 'user' parameter so actions which have dedicated classes can reference their properties (anonymous classes wouldn't require it
       since they have the class reference itself */
    public abstract void commit(Actor user, Actor target);

    public String toString() { return TextFormatter.formatAction(type, name, description); }

    public enum ActionType
    {
        ATTACK("\uD83D\uDCA5"), STATUS("☯️"), OTHER("❓");

        private String val;

        ActionType(String assignedValue) { val = assignedValue; }

        @Override public String toString() { return val; }

    }
}
