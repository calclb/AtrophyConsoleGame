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

}
