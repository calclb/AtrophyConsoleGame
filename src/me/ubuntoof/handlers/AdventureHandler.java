package me.ubuntoof.handlers;

import me.ubuntoof.Party;
import me.ubuntoof.actions.*;
import me.ubuntoof.entities.Druid;
import me.ubuntoof.entities.Entity;
import me.ubuntoof.entities.Golem;
import me.ubuntoof.passives.Immunity;
import me.ubuntoof.passives.Inflame;

public class AdventureHandler
{
    private Areas currentArea;
    private final int msTypeDelay = 150;

    public AdventureHandler()
    {
        currentArea = Areas.VOID;
    }

    public AdventureHandler(Areas area)
    {
        this.currentArea = area;
    }

    public void begin()
    {

        Entity player = Entity.generatePlayerContainerFromDialogue();
        player.setPlayerControlled(true);
        player.addActions(Bop.class, Panic.class, FireBlast.class);
        player.addPassives(Inflame.class);
        Golem golem = new Golem();

        Entity p2 = Entity.generatePlayerContainerFromDialogue();
        p2.setPlayerControlled(false);
        p2.addActions(EsophagealBurn.class, Combah.class, Panacea.class);
        p2.addPassives(Immunity.class);
        Druid druid = new Druid();

        Party playerOneTeam = new Party("P1 Team", player, golem);
        Party playerTwoTeam = new Party("P2 Team", p2, druid);

        Tourney tourney = new Tourney(3, 4, playerOneTeam, playerTwoTeam);

        tourney.runTourney();
//        Battle battle = new Battle(new Team[]{new Team(pTem), new Team(p2Tem)}, 4);
//        battle.run();
    }
}

enum Areas
{
    VOID("Void");

    private final String label;

    Areas(String label)
    {
        this.label = label;
    }

    @Override public String toString() { return label; }
}
