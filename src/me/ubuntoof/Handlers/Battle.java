package me.ubuntoof.Handlers;

import me.ubuntoof.Characters.*;
import me.ubuntoof.Utils.Colorizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class Battle {

    Random random = new Random();

    Battle(Actor[] allies, Actor[] enemies)
    {
        ArrayList<Actor> combatants = new ArrayList<>();
        ArrayList<Actor> friendlies = new ArrayList<>(Arrays.asList(allies));
        ArrayList<Actor> foes = new ArrayList<>(Arrays.asList(enemies));

        combatants.addAll(friendlies);
        combatants.addAll(foes);


        /*System.arraycopy(allies, 0, combatants, 0, allies.length);
        System.arraycopy(enemies, 0, combatants, allies.length, enemies.length);*/

        boolean areAlliesAlive = false;
        boolean areEnemiesAlive = false;

        do
        {
            for(Actor friendly : friendlies)
            {
                if(friendly.isAlive())
                {
                    System.out.println(Colorizer.GRAY + "Ally " + friendly.toString() + " is alive.");
                    areAlliesAlive = true;
                    break;
                } else areAlliesAlive = false;
            }

            for(Actor foe : foes)
            {
                if(foe.isAlive())
                {
                    System.out.println(Colorizer.GRAY + "Foe " + foe.toString() + " is alive.");
                    areEnemiesAlive = true;
                    break;
                } else areEnemiesAlive = false;
            }

            for(int i = 0; i < combatants.size(); i++)
            {
                // TODO do combat logic
                // displayBattleStats();
                // if(combatants.get(i) instanceof Player) combatants.get(i).doAction(getUserResponse());

            }

        } while(areAlliesAlive && areEnemiesAlive);
        // while members of either side is alive
            // go through turn
            // show battle status
                // prompt user/enemy to use a move
                // display their choice, then update status
    }

    public ArrayList<Actor> createEnemies(int count)
    {
        Random r = new Random();
        ArrayList<Actor> enemies = new ArrayList<>();

        for(int i = 0; i < count; i++)
        {
            enemies.add(matchEnemyIndex(r.nextInt(2)));
        }
        return enemies;

    }

    private Actor matchEnemyIndex(int index) {

        switch (index)
        {
            case 0: return new Bandit(random.nextInt(10));
            case 1: return new Goblin(random.nextInt(10));
        }

        return null;
    }

}
