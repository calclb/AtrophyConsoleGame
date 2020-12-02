package me.ubuntoof.utils;

import me.ubuntoof.Stats;
import me.ubuntoof.actions.Action;

public class TextFormatter
{
    public static void imitatePrinting(String s, int msDelay)
    {
        print("");
        for(char c : s.toCharArray())
        {
            try
            {
                Thread.sleep(msDelay);
            }
            catch (InterruptedException ignored) {}

            System.out.print(c);
        }
    }

    public static void displayOnSecondCounter(String s, int seconds)
    {
        println();
        for( ; seconds > 0; seconds--)
        {
            try
            {
                print(s + " " + Colorizer.BOLD + seconds + "s");
                print("\r".repeat(s.length()));
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }
    }

    public static String formatInfo(String s) { return Colorizer.BOLD + Colorizer.BLUE + "[i] " + Colorizer.RESET + Colorizer.BLUE + Colorizer.UNDERLINE + s + Colorizer.RESET; }
    public static String formatWarning(String s) { return Colorizer.BOLD + Colorizer.YELLOW + "[⚠️] " + Colorizer.RESET + Colorizer.YELLOW + Colorizer.UNDERLINE + s + Colorizer.RESET; }
    public static String formatError(String s) { return Colorizer.BOLD + Colorizer.RED + "[!] " + Colorizer.RESET + Colorizer.RED + Colorizer.UNDERLINE + s + Colorizer.RESET; }

    public static String formatAction(Action action) { return "(" + Colorizer.GRAY + action.getActionType() + Colorizer.RESET + ") " + action.getName() + " ― " + action.getDescription(); }

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
        return sb.toString() + " ";
    }

    public static String superscript(String str)
    {
        str = str.replaceAll("0", "⁰");
        str = str.replaceAll("1", "¹");
        str = str.replaceAll("2", "²");
        str = str.replaceAll("3", "³");
        str = str.replaceAll("4", "⁴");
        str = str.replaceAll("5", "⁵");
        str = str.replaceAll("6", "⁶");
        str = str.replaceAll("7", "⁷");
        str = str.replaceAll("8", "⁸");
        str = str.replaceAll("9", "⁹");
        return str;
    }

    public static String subscript(String str)
    {
        str = str.replaceAll("0", "₀");
        str = str.replaceAll("1", "₁");
        str = str.replaceAll("2", "₂");
        str = str.replaceAll("3", "₃");
        str = str.replaceAll("4", "₄");
        str = str.replaceAll("5", "₅");
        str = str.replaceAll("6", "₆");
        str = str.replaceAll("7", "₇");
        str = str.replaceAll("8", "₈");
        str = str.replaceAll("9", "₉");
        return str;
    }

    public static void print(String s)
    {
        System.out.print(Colorizer.indent() + s);
    }
    public static void println(String s)
    {
        System.out.println(Colorizer.indent() + s);
    }
    public static void println()
    {
        System.out.println();
    }
}
