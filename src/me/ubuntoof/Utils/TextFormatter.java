package me.ubuntoof.Utils;

import me.ubuntoof.Actions.Action.ActionType;

public class TextFormatter {

    public static void imitateTyping(String s, int msDelay)
    {
        for(char c : s.toCharArray())
        {
            try
            {
                Thread.sleep(msDelay);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            System.out.print(c);
        }
    }

    public static String formatAction(ActionType type, String n, String d) { return "(" + type + ") " + n + " - " + d; }

    public static String formatAilment(String i, String n, String d) { return i; }

    public static String formatStat(String i, String n, String d) { return "[" + i + "] " + n + " - " + d; }
}
