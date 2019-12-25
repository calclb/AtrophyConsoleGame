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

    public static void displayOnSecondCounter(String s, int seconds)
    {
        System.out.println();
        for( ; seconds > 0; seconds--)
        {
            try
            {
                System.out.print("\r\r\r" + s + " " + Colorizer.BOLD + seconds + "s");
                Thread.sleep(200);
            } catch (InterruptedException e) {}
        }
        System.out.print("\r");
    }

    public static String formatInfo(String s) { return Colorizer.BOLD + Colorizer.BLUE + "[ℹ] " + Colorizer.RESET + Colorizer.BLUE + Colorizer.UNDERLINE + s + Colorizer.RESET; }
    public static String formatWarning(String s) { return Colorizer.BOLD + Colorizer.YELLOW + "[⚠️] " + Colorizer.RESET + Colorizer.YELLOW + Colorizer.UNDERLINE + s + Colorizer.RESET; }
    public static String formatError(String s) { return Colorizer.BOLD + Colorizer.RED + "[！] " + Colorizer.RESET + Colorizer.RED + Colorizer.UNDERLINE + s + Colorizer.RESET; }

    public static String formatAction(Action action) { return "(" + action.getType() + ") " + action.getName() + " ― " + action.getDescription(); }

    public static String formatAilment(String i, String n, String d) { return i; }

    public static String formatStat(Stats stat) { return "[" + stat.getIcon() + "] " + stat.getName() + " ― " + stat.getDescription(); }

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
        double percentage = (double)base / max;
        StringBuilder sb = new StringBuilder();

        for(int i = 1; i <= barLength; i++)
        {
            sb.append(percentage >= (double)i / barLength ? onColor : offColor);
            sb.append(unit);
        }
        return sb.toString();
    }

    public static String formatAsHealthBar(String unit, int base, int max, int barLength, String onString, String offString)
    {

        double healthPercentage = (double)base / max;
        String on = (healthPercentage > 1d ? Colorizer.LIGHT_CYAN : healthPercentage > 0.5d ? Colorizer.GREEN : healthPercentage > 0.25d ? Colorizer.YELLOW : Colorizer.RED) + onString;
        String off = Colorizer.GRAY + offString;

        StringBuilder sb = new StringBuilder();

        for(int i = 1; i <= barLength; i++)
        {
            sb.append(healthPercentage >= (double)i / barLength ? on : off);
            sb.append(unit);
        }
        return sb.toString();
    }

}
