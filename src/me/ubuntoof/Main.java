package me.ubuntoof;

import me.ubuntoof.Handlers.AdventureHandler;
import me.ubuntoof.Utils.Colorizer;
import me.ubuntoof.Utils.UserInputReader;

public class Main {
    public static void main(String[] args) {


        Colorizer.printDivider(60);

        System.out.println(Colorizer.PURPLE + Colorizer.BOLD + "\n\uD83D\uDD31 Atrophy " + Colorizer.RESET + Colorizer.PURPLE + "|" +
                Colorizer.WHITE + " A rogue-like console game with battles. " +
                Colorizer.LIGHT_GRAY + Colorizer.ITALIC + "\n             Input 'start' to start.\n" + Colorizer.RESET);

        Colorizer.printDivider(60);

        boolean started = UserInputReader.getResponse().equals("start");

        while(true)
        {
            if(started)
            {
                Colorizer.clear();
                AdventureHandler adventureHandler = new AdventureHandler();
                break;
            }
            System.out.println(Colorizer.ITALIC + "Just say the word when you're ready to go." + Colorizer.RESET);
            started = UserInputReader.getResponse().equals("start");
        }
    }
}
