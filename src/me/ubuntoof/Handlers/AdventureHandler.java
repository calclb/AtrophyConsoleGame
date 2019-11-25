package me.ubuntoof.Handlers;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Characters.Player;
import me.ubuntoof.Utils.Colorizer;
import me.ubuntoof.Utils.TextFormatter;
import me.ubuntoof.Utils.UserInputReader;

import javax.smartcardio.TerminalFactory;


public class AdventureHandler {

    private Areas currentArea;
    private int msTypeDelay = 150;

    public AdventureHandler()
    {
        currentArea = Areas.VOID;
    }

    public AdventureHandler(Areas area)
    {
        this.currentArea = area;
    }

    public void startBattle(Actor player, int enemies)
    {
        Actor[] friendlies = new Actor[1];
        friendlies[0] = player;

        Battle battle = new Battle(friendlies, enemies);
    }

    public void begin() {

        TextFormatter.imitatePrinting(new String[]{
                Colorizer.ITALIC + "...I know it may be rude to ask, but I can't place a finger on what I should call you.\n"},
                new int[]{0}); // 1000

        String playerName = UserInputReader.getResponse();

        String[] strings = {Colorizer.ITALIC + "I hope you're not playing me for a fool. ",
                "Please understand, ",
                "in all honesty, ",
                "that I'm in much of the same predicament as you.\n" + Colorizer.RESET};

        int[] delays = {0, 0, 0, 0};
        //int[] delays = {1600, 1100, 1000, 1150};

        TextFormatter.imitatePrinting(strings, delays);

        Player player = new Player(playerName, Player.getDefaultActions(), 1);

        Battle battle = new Battle(new Actor[]{player}, 2);
        battle.startBattle();
    }

}

enum Areas
{
    VOID("Void");

    private String label;

    Areas(String label)
    {
        this.label = label;
    }
}
