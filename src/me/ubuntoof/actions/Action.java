package me.ubuntoof.actions;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.utils.TextFormatter;

import java.util.Random;

/**
 * Actions are the moves used by Actors (characters) on their turn.
 */
public abstract class Action
{
    private final ActionType actionType;

    public final String name;
    public final String description;
    public final boolean supportive; // used in enemy decision tree to choose proper targets
    public final boolean usesDirectContact;
    public final boolean requiresTarget; // might be AoE or a random target

    protected final static Random random = new Random();

    protected Action(ActionType type, String name, String description, boolean supportive, boolean usesDirectContact)
    {
        this.actionType = type;
        this.name = name;
        this.description = description;
        this.supportive = supportive;
        this.usesDirectContact = usesDirectContact;
        requiresTarget = true;
    }

    protected Action(ActionType type, String name, String description, boolean supportive, boolean usesDirectContact, boolean requiresTarget)
    {
        this.actionType = type;
        this.name = name;
        this.description = description;
        this.supportive = supportive;
        this.usesDirectContact = usesDirectContact;
        this.requiresTarget = requiresTarget;
    }

    /**
     * Returns a constant defined by the {@code ActionType} enum. This defines how the attack is meant to deal damage.
     * @see ActionType
     */
    public ActionType getActionType() { return actionType; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isSupportive() { return supportive; }
    public boolean usesDirectContact() { return usesDirectContact; }
    public boolean getRequiresTarget() { return requiresTarget; }

    /* has to have 'user' parameter so actions which have dedicated classes can reference their properties (anonymous action classes wouldn't require it
       since they already have the Actor reference) */
    public abstract void commit(Actor user, Actor target);

    public String toString() { return TextFormatter.formatAction(this); }

    public static boolean isAttack(ActionType at) { return at != ActionType.STATUS; }


    public enum ActionType
    {
        /*ATTACK("\uD83D\uDCA5"),*/
        STATUS("☯️"),

        SLASHING("Slashing"),
        PIERCING("Piercing"),
        BLUDGEONING("Bludgeoning"),
        CORROSION("Corrosion"),
        NOXIOUS("Noxious"),
        RADIANT("Radiant"),
        SCORCHING("Scorching"),
        FREEZING("Freezing"),
        ELECTRIC("Electric"),
        FORCE("Force"),
        PSYCHIC("Psychic");

        private final String val;
        ActionType(String assignedValue) { val = assignedValue; }
        @Override public String toString() { return val; }
    }
}