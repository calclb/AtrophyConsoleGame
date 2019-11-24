package me.ubuntoof;

public enum Stats {
    MAX_HEALTH("♥", "Max Health", "Maximum health capacity."),
    HEALTH("♡", "Health", "Used to determine the living state of a combatant."),
    STRENGTH("\uD83C\uDFCB", "Strength", "Damage multiplier applied to various attacks. More strength? More damage."),
    DEFENSE("\uD83D\uDEE1", "Defense", "Reduces damage from attacks by the value of this stat."),
    ARMOR("\uD83D\uDD30", "Armor", "Completely negates attack damage less than the value of this stat."),
    SPEED("\uD83C\uDFC3", "Speed", "Determines turn order, among other things.");

    private String icon;
    private String name;
    private String desc;

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
