package me.ubuntoof;

public class Action {

    private ActionType type;
    private String name;
    private String description;

    Action(ActionType type, String name, String description)
    {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public ActionType getType() { return type; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    enum ActionType
    {
        ATTACK, STATUS, OTHER
    }
}
