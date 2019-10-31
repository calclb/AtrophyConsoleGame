package me.ubuntoof.Handlers;

import me.ubuntoof.Characters.Actor;


public class AdventureHandler {

    public void startBattle(Actor player, int enemies)
    {
        Actor[] friendlies = new Actor[1];
        Actor[] foes = new Actor[enemies];
        friendlies[0] = player;

        Battle battle = new Battle(friendlies, foes);
    }

}
