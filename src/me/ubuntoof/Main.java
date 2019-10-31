package me.ubuntoof;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Handlers.AdventureHandler;
import me.ubuntoof.Utils.Colorizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println(Colorizer.GRAY + Colorizer.STRIKETHROUGH + "                                                  " + Colorizer.RESET + "\n");
        System.out.println(Colorizer.PURPLE + Colorizer.BOLD + "Atrophy " + Colorizer.RESET + Colorizer.PURPLE + "|" + Colorizer.WHITE + " A rogue-like text game with battles. " + Colorizer.LIGHT_GRAY + Colorizer.ITALIC + "\n          Input 'start' to start." + Colorizer.RESET);
        System.out.println(Colorizer.GRAY + Colorizer.STRIKETHROUGH + "                                                  " + Colorizer.RESET);

        System.out.print(Colorizer.LIGHT_GREEN + "〉");
        boolean started = getResponse().equals("start");

        if(started)
        {
            AdventureHandler ah = new AdventureHandler();

            Action[] playerActions = new Action[3];
            playerActions[0] = new Action(Action.ActionType.ATTACK, "Punch", "Deal 4 damage to an enemy.");
            playerActions[1] = new Action(Action.ActionType.ATTACK, "Poke", "Deal 2 damage to an enemy. Ignores resistances.");
            playerActions[2] = new Action(Action.ActionType.STATUS, "Heal", "Heal yourself or an ally for 3 health.");

            Action[] enemyActions = new Action[2];
            enemyActions[0] = new Action(Action.ActionType.ATTACK, "Punch", "Deal 3 damage to an enemy.");
            enemyActions[1] = new Action(Action.ActionType.ATTACK, "Kick", "Deal 2 damage to an enemy. ");

            Actor player = new Actor(playerActions, 10);

            ah.startBattle(player, 2);
            Colorizer.clear();
        }
    }

    static String getResponse() throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
}
