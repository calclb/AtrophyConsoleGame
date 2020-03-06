package me.ubuntoof;

import me.ubuntoof.handlers.AdventureHandler;
import me.ubuntoof.utils.Colorizer;
import me.ubuntoof.utils.UserInputReader;

public class Main
{
    public static String[] myArgs;
    public static boolean useTerminal = false;
    public static void main(String[] args)
    {
        handleArgumentConfig(args);

        print(Colorizer.getDivider(60));
        println();
        println(Colorizer.PURPLE + Colorizer.BOLD + "\uD83D\uDD31 Atrophy " + Colorizer.RESET + Colorizer.PURPLE + "|" +
                Colorizer.WHITE + " A rogue-like console game with battles. " +
                Colorizer.LIGHT_GRAY + Colorizer.ITALIC);
        println("             Input 'start' to start.\n" + Colorizer.RESET);

        print(Colorizer.getDivider(60));

        boolean started = UserInputReader.getResponse().equals("start");

        while(true)
        {
            if(started)
            {
                Colorizer.clear();
                AdventureHandler adventureHandler = new AdventureHandler();
                adventureHandler.begin();
                break;
            }
            println(Colorizer.ITALIC + "Just say the word when you're ready to go." + Colorizer.RESET);
            started = UserInputReader.getResponse().equals("start");
        }
    }

    private static void handleArgumentConfig(String[] args)
    {
        myArgs = args;
        for(String a : args) if (a.equalsIgnoreCase("-fancy"))
        {
            useTerminal = true;
            break;
        }
    }

    private static void print(String s)
    {
        Colorizer.print(s); 
    }
    private static void println(String s)
    {
        Colorizer.println(s);
    }
    private static void println()
    {
        Colorizer.println();
    }
}
