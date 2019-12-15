package me.ubuntoof.Handlers;

import me.ubuntoof.Actions.*;
import me.ubuntoof.Characters.*;
import me.ubuntoof.Passives.Inflame;
import me.ubuntoof.Team;
import me.ubuntoof.Utils.Colorizer;
import me.ubuntoof.Utils.TextFormatter;
import me.ubuntoof.Utils.UserInputReader;

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

        Player player = new Player(playerName, new Action[]{new Bop(), new Combah(), new Panic(), new EsophagealBurn(), new Taze(), new Panacea()}, 20);
        player.setPassive(new Inflame(player));
        Spaelcaster spaelcaster = new Spaelcaster(30);
        Druid druid = new Druid(30);
        Player ally = new Player("Allydude", new Action[]{new Bop(), new Combah()}, 10);

        Team playerTeam = new Team(new Actor[]{player, spaelcaster});
        Team otherPlayerTeam = new Team(new Actor[]{ally, druid});

        Battle battle = new Battle(new Team[]{playerTeam, otherPlayerTeam}, 4);
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
