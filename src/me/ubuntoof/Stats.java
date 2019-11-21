package me.ubuntoof;

public enum Stats {
    MAXHEALTH(""), HEALTH(""), STRENGTH(""), SPEED("");

    private String stringIcon;

    public String getStringIcon() { return stringIcon; }

    Stats(String icon)
    {
        stringIcon = icon;
    }
}
