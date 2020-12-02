package me.ubuntoof.modifiers;

import me.ubuntoof.Stats;
import me.ubuntoof.utils.Colorizer;

import java.text.NumberFormat;

public class StatModifier extends Decrementable
{
    private final Stats type;
    private double multiplier;
    private static final NumberFormat pf = NumberFormat.getPercentInstance();

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
    public void setMultiplier(double m) { multiplier = m; }

    public String toString()
    {
        String color = multiplier > 1 ? Colorizer.BLUE : multiplier < 1 ? Colorizer.RED : Colorizer.RESET;
        double percentRaised = multiplier - 1;
        return Colorizer.REVERSE + color +  type.getIcon() + " " + (percentRaised > 0 ? "+" : "") + pf.format(percentRaised) + " (" + getDurationInTurns() + "t)" + Colorizer.RESET;
    }

    @Override public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            e.printStackTrace();
            return new CloneNotSupportedException();
        }
    }
}
