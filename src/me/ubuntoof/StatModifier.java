package me.ubuntoof;

public class StatModifier {

    private Stats[] stats;
    private int turnDuration;

    StatModifier(Stats[] stats, int turnDuration)
    {
        this.stats = stats;
        this.turnDuration = turnDuration;
    }
}
