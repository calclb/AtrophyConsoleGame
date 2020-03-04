package me.ubuntoof;

import me.ubuntoof.handlers.AdventureHandler;
import me.ubuntoof.utils.Colorizer;
import me.ubuntoof.utils.UserInputReader;

public class Main
{
    public static void main(String[] args)
    {


        System.out.print(Colorizer.getDivider(60));

        System.out.println(Colorizer.PURPLE + Colorizer.BOLD + "\n\uD83D\uDD31 Atrophy " + Colorizer.RESET + Colorizer.PURPLE + "|" +
                Colorizer.WHITE + " A rogue-like console game with battles. " +
                Colorizer.LIGHT_GRAY + Colorizer.ITALIC + "\n             Input 'start' to start.\n" + Colorizer.RESET);

        System.out.print(Colorizer.getDivider(60));

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
            System.out.println(Colorizer.ITALIC + "Just say the word when you're ready to go." + Colorizer.RESET);
            started = UserInputReader.getResponse().equals("start");
        }
    }
}
