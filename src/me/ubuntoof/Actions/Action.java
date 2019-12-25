package me.ubuntoof.Actions;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Utils.TextFormatter;

import javax.xml.soap.Text;
import java.util.Random;

public abstract class Action {

    private ActionType type;
    private String name;
    private String description;
    public final boolean supportive;
    public final boolean usesContact;
    public final boolean requiresTarget;
    protected final static Random random = new Random();

    protected Action(ActionType type, String name, String description, boolean supportive, boolean usesContact)
    {
        this.type = type;
        this.name = name;
        this.description = description;
        this.supportive = supportive;
        this.usesContact = usesContact;
        requiresTarget = true;
    }

    protected Action(ActionType type, String name, String description, boolean supportive, boolean usesContact, boolean requiresTarget)
    {
        this.type = type;
        this.name = name;
        this.description = description;
        this.supportive = supportive;
        this.usesContact = usesContact;
        this.requiresTarget = requiresTarget;
    }

    public ActionType getType() { return type; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isSupportive() { return supportive; }
    public boolean getUsesContact() { return usesContact; }
    public boolean getRequiresTarget() { return requiresTarget; }

    /* has to have 'user' parameter so actions which have dedicated classes can reference their properties (anonymous classes wouldn't require it
       since they have the class reference itself) */
    public abstract void commit(Actor user, Actor target);

    public String toString() { return TextFormatter.formatAction(this); /*TextFormatter.formatAction(type, name, description); */}

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
