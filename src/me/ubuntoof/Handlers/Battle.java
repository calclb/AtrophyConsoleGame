package me.ubuntoof.Handlers;

import me.ubuntoof.Characters.*;
import me.ubuntoof.Listeners.TurnListener;
import me.ubuntoof.Modifiers.GlobalCondition;
import me.ubuntoof.Utils.Colorizer;

import java.util.*;

public class Battle {

    private Set<GlobalCondition> globalConditions = Collections.emptySet();
    private Actor[] combatants;
    private Actor[] allies;
    private Actor[] enemies;
    private List<TurnListener> turnListeners = new ArrayList<>();

    public Battle(Actor[] allies, int numEnemies)
    {
        this.allies = allies;
        this.enemies = createEnemies(numEnemies);
        combatants = new Actor[allies.length + enemies.length];
        System.arraycopy(allies, 0, combatants, 0, allies.length);
        System.arraycopy(enemies, 0, combatants, 0, combatants.length);

    }

    public Battle(Actor[] allies, Actor[] enemies)
    {
        this.allies = allies;
        this.enemies = enemies;
        combatants = new Actor[allies.length + enemies.length];
        System.arraycopy(allies, 0, combatants, 0, allies.length);
        System.arraycopy(enemies, 0, combatants, 0, combatants.length);
    }

    public void startBattle()
    {
        turnListeners.addAll(Arrays.asList(combatants));
        // TODO do this
        // sort combatants every turn by speed
        // run through combatant listeners every turn

        for(TurnListener tl : turnListeners) tl.onBattleStarted();

        do
        {
            Arrays.sort(combatants);

            for(GlobalCondition gc : globalConditions) gc.applyEffects(combatants);

        } while(true); // TODO while there are combatants of both sides; end battle otherwise.
    }

    private Actor[] createEnemies(int count)
    {
        Random r = new Random();
        Actor[] enemies = new Actor[count];

        for(int i = 0; i < count; i++)
        {
            enemies[i] = matchEnemyIndex(r.nextInt(EnemyTypes.variants()));
        }
        return enemies;

    }

    private Actor matchEnemyIndex(int type) { return EnemyTypes.values()[type].getType(); }

    public Actor[] getCombatants() { return combatants; }

    public Actor[] getOpposition(Actor user)
    {
        for(Actor actor : allies) if(actor == user)
        {
            return enemies;
        }

        for(Actor actor : enemies) if(actor == user)
        {
            return allies;
        }

        throw new IllegalArgumentException("Actor " + user + " doesn't exist in either the allies' or enemies' arrays.");
    }

}

enum EnemyTypes
{
    GOBLIN(Goblin.class),
    BANDIT(Bandit.class);

    private Class type;

    EnemyTypes(Class c)
    {
        type = c;
    }

    public Actor getType() { try {return (Actor)type.newInstance();} catch(Exception ignored){} return null;}

    static int variants() { return EnemyTypes.values().length; }
}
