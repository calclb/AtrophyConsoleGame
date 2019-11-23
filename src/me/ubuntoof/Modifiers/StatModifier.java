package me.ubuntoof.Modifiers;

import me.ubuntoof.Stats;

public class StatModifier extends Decrementable {

    private Stats type;
    private double multiplier;

    public StatModifier(Stats statType, double multiplier)
    {
        type = statType;
        this.multiplier = multiplier;
        setPermanent();
    }

    public StatModifier(Stats statType, double multiplier, int turnDuration)
    {
        type = statType;
        this.multiplier = multiplier;
        setDurationInTurns(turnDuration);
    }

    public Stats getModifierType() { return type; }
    public double getMultiplier() { return multiplier; }
}
