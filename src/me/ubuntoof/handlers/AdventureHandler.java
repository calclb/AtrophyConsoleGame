package me.ubuntoof.handlers;

import me.ubuntoof.Team;
import me.ubuntoof.actions.*;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.characters.Druid;
import me.ubuntoof.characters.Player;
import me.ubuntoof.characters.Spaelcaster;
import me.ubuntoof.passives.Bloodlust;
import me.ubuntoof.passives.Explosive;
import me.ubuntoof.passives.Immunity;
import me.ubuntoof.passives.Persistence;
import me.ubuntoof.utils.Colorizer;
import me.ubuntoof.utils.TextFormatter;
import me.ubuntoof.utils.UserInputReader;

import java.util.ArrayList;
import java.util.List;

public class AdventureHandler
{
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

        TextFormatter.imitatePrinting(Colorizer.ITALIC + "...I know it may be rude to ask, but I can't place a finger on what I should call you.\n", 10);

        String playerName = UserInputReader.getResponse();

        TextFormatter.imitatePrinting(Colorizer.ITALIC + "I hope you're not playing me for a fool. Please understand, in all honesty, that I'm in much of the same predicament as you.\n" + Colorizer.RESET, 10);

        List<Action> p1moves = new ArrayList<>();
        p1moves.add(new Combah()); p1moves.add(new Bop()); p1moves.add(new EsophagealBurn()); p1moves.add(new Taze());
        Player player = new Player(playerName, p1moves, 50);
        player.getPassives().add(new Persistence()); player.getPassives().add(new Bloodlust());

        Spaelcaster spaelcaster = new Spaelcaster(30);
        Druid druid = new Druid(30);

        List<Action> p2moves = new ArrayList<>();
        p2moves.add(new Panic()); p2moves.add(new FireBlast()); p2moves.add(new Panacea());
        Player player2 = new Player(playerName + "'s doppelganger", p2moves, 30);
        player2.getPassives().add(new Immunity()); player2.getPassives().add(new Explosive());


        Team playerTeam = new Team(new Actor[]{spaelcaster, /*player2*/});
        Team otherPlayerTeam = new Team(new Actor[]{druid, player, new Spaelcaster(10), new Spaelcaster(10)});


        Battle battle = new Battle("Coliseum", new Team[]{playerTeam, otherPlayerTeam}, 4, false);
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
