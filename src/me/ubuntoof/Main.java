package me.ubuntoof;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Handlers.AdventureHandler;
import me.ubuntoof.Utils.Colorizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
    public static void main(String[] args) throws IOException {


        Colorizer.printDivider(50);
        System.out.println(Colorizer.PURPLE + Colorizer.BOLD + "\uD83D\uDD31 Atrophy " + Colorizer.RESET + Colorizer.PURPLE + "|" + Colorizer.WHITE + " A rogue-like text game with battles. " + Colorizer.LIGHT_GRAY + Colorizer.ITALIC + "\n            Input 'start' to start.\n" + Colorizer.RESET);
        Colorizer.printDivider(50);

        System.out.print(Colorizer.LIGHT_GREEN + "〉");
        boolean started = getResponse().equals("start");


        if(started)
        {
            Colorizer.clear();
            AdventureHandler ah = new AdventureHandler();
        }
    }

    private static String getResponse() throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
}
