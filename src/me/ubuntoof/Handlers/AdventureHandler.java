package me.ubuntoof.Handlers;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Utils.TextFormatter;


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

        TextFormatter.imitateTyping("...I know it may be rude to ask, but I can't place a finger on what I should call you.", 100);
        // TODO begin the storyline.
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
