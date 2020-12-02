package me.ubuntoof;

import java.util.List;

public enum Stats
{

    STAMINA("♥️", "Stamina", "Mental and physical well-being of a of a character."),
    STRENGTH("\uD83D\uDCAA", "Strength", "Describes the capability of a character to move obstacles and fight via brute force."),
    AGILITY("\uD83D\uDCA8", "Agility", "Describes a character's speed and evasion capabilities.");

    private final String icon;
    private final String name;
    private final String desc;

    public static final List<Stats> VALUES = List.of(values());
    public static final int SIZE = VALUES.size();

    public String getIcon() { return icon; }
    public String getName() { return name; }
    public String getDescription() { return desc; }

    Stats(String icon, String name, String desc)
    {
        this.icon = icon;
        this.name = name;
        this.desc = desc;
    }
}
