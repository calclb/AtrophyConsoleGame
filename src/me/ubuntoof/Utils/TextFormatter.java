package me.ubuntoof.Utils;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Actions.Action.ActionType;
import me.ubuntoof.Stats;

import java.text.DecimalFormat;
import java.util.HashMap;

public class TextFormatter {

    public static void imitatePrinting(String[] strings, int[] msDelays)
    {
        for(int i = 0; i < msDelays.length; i++)
        {
            try
            {
                Thread.sleep(msDelays[i]);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            System.out.print(strings[i]);
        }
    }

    public static String formatAction(ActionType type, String name, String description) { return "(" + type + ") " + name + " - " + description; }

    public static String formatAilment(String i, String n, String d) { return i; }

    public static String formatStat(Stats stat) { return "[" + stat.getIcon() + "] " + stat.getName() + " - " + stat.getDescription(); }

    public static String formatAsProgressBar(int base, int max, int barLength)
    {
        double healthPercentage = (double)base / max;
        StringBuilder str = new StringBuilder();

        for(int i = 1; i <= barLength; i++)
        {
            str.append(healthPercentage >= (double)i / barLength ? Colorizer.LIGHT_GREEN : Colorizer.RED);
            str.append("▪");
        }
        return str.toString();
    }

    public static String formatAsProgressBar(String unit, int base, int max, int barLength, String onColor, String offColor)
    {
        double healthPercentage = (double)base / max;
        StringBuilder sb = new StringBuilder();

        for(int i = 1; i <= barLength; i++)
        {
            sb.append(healthPercentage >= (double)i / barLength ? onColor : offColor);
            sb.append(unit);
        }
        return sb.toString();
    }

}
