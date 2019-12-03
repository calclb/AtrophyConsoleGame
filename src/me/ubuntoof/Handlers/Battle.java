package me.ubuntoof.Handlers;

import me.ubuntoof.Characters.*;
import me.ubuntoof.Listeners.BattleInteractions;
import me.ubuntoof.Modifiers.*;
import me.ubuntoof.Modifiers.GlobalConditions.*;
import me.ubuntoof.Utils.Colorizer;
import me.ubuntoof.Utils.TextFormatter;

import java.util.*;

public class Battle {

    private Set<GlobalCondition> globalConditions = new HashSet<>();
    private Actor[] combatants;
    private Actor[] allies;
    private Actor[] enemies;
    private List<BattleInteractions> battleInteractionsListeners = new ArrayList<>();
    private int turn;

    public Battle(Actor[] allies, int numEnemies)
    {
        this.allies = allies;
        this.enemies = createEnemies(numEnemies);
        combatants = new Actor[allies.length + enemies.length];
        System.arraycopy(allies, 0, combatants, 0, allies.length);
        System.arraycopy(enemies, 0, combatants, allies.length, enemies.length);
    }

    public Battle(Actor[] allies, Actor[] enemies)
    {
        this.allies = allies;
        this.enemies = enemies;
        combatants = new Actor[allies.length + enemies.length];
        System.arraycopy(allies, 0, combatants, 0, allies.length);
        System.arraycopy(enemies, 0, combatants, allies.length, enemies.length);
    }

    public void startBattle()
    {
        battleInteractionsListeners.addAll(Arrays.asList(combatants));

        globalConditions.add(new Hail());
        // TODO sort combatants every turn by speed
        // run through combatant listeners every turn

        for(BattleInteractions bi : battleInteractionsListeners) bi.onBattleStarted(this);

        do
        {
            turn++;
            displayGlobalBattle();
            for(BattleInteractions bi : battleInteractionsListeners) bi.onGlobalTurnStarted();


            for(Actor actor : combatants)
            {
                if(!actor.isAlive()) continue;
                System.out.println(Colorizer.RESET + "\n" + Colorizer.REVERSE + actor.getName() + "'s turn." + Colorizer.RESET);
                if(!actor.isAlive()) continue;
                for(StatModifier sm : actor.getStatModifiers()) sm.decrementTurnsRemaining();
                actor.onUserTurn();
            }

            for(GlobalCondition gc : globalConditions)
            {
                gc.applyEffects(combatants);
                assert gc.getDurationInTurns() >= 0;
                if(gc.getDurationInTurns() == 0) globalConditions.remove(gc);
            }

            for(BattleInteractions bi : battleInteractionsListeners) bi.onGlobalTurnEnded();

        } while(checkCombatantSidesAreAlive());

        for(BattleInteractions bi : battleInteractionsListeners) bi.onBattleEnded();
        for(Actor actor : allies) if(actor.isAlive()) System.out.println("");
    }

    private void displayGlobalBattle()
    {
        Colorizer.printDivider(60);
        System.out.println(Colorizer.GRAY + "Turn " + turn + "" + Colorizer.RESET);

        for(GlobalCondition gc : globalConditions) System.out.println(gc.getIcon() + " " + gc.getName() + " - " + gc.getDurationInTurns() + " turn" + (gc.getDurationInTurns() == 1 ? "" : "s") + " remaining" + Colorizer.RESET);
        System.out.println();

        for(int i = 0; i < allies.length; i++) if(allies[i].isAlive())
        {
            Actor actor = allies[i];
            System.out.println(Colorizer.LIGHT_GRAY + "[" + i + "] " + Colorizer.BLUE + actor.getName() + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getMaxHealth() + ") " +
                    TextFormatter.formatAsProgressBar(actor.getHealth(), actor.getMaxHealth(), 10) + " " + returnAilmentIcons(actor) + Colorizer.RESET);
        }

        for(int i = 0; i < enemies.length; i++) if(enemies[i].isAlive())
        {
            Actor actor = enemies[i];
            System.out.println(Colorizer.LIGHT_GRAY + "[" + (i + allies.length) + "] " + Colorizer.RED + actor.getName() + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getMaxHealth() + ") " +
                    TextFormatter.formatAsProgressBar(actor.getHealth(), actor.getMaxHealth(), 10) + " " + returnAilmentIcons(actor) + Colorizer.RESET);
        }

        System.out.println(Colorizer.GRAY + "\nSee /stats <target> to display properties of a character." + Colorizer.RESET);
        Colorizer.printDivider(60);
    }

    private String returnAilmentIcons(Actor actor) {
        StringBuilder sb = new StringBuilder();
        for(Ailment ailment : actor.getAilments()) sb.append(ailment.getIcon());
        return sb.toString();
    }

    private boolean checkCombatantSidesAreAlive()
    {
        boolean areAlliesAlive = false;
        boolean areEnemiesAlive = false;
        for(Actor actor : allies) if(!areAlliesAlive && actor.isAlive()) areAlliesAlive = true;
        for(Actor actor : enemies) if(!areEnemiesAlive && actor.isAlive()) areEnemiesAlive = true;
        return areAlliesAlive && areEnemiesAlive;
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

    private Actor matchEnemyIndex(int type) {
        Random random = new Random();

        switch (type)
        {
            case 0: return new Bandit(random.nextInt(15));
            case 1: return new Goblin(random.nextInt(15));
            case 2: return new Spaelcaster(random.nextInt(15));
        }
        return null;
    }

    public Actor[] getCombatants() { return combatants; }

    public Actor[] getOpposition(Actor user)
    {
        for(Actor actor : allies) if(actor == user) return enemies;
        for(Actor actor : enemies) if(actor == user) return allies;
        throw new IllegalArgumentException("Actor " + user + " doesn't exist in either the allies' or enemies' arrays.");
    }

    public Actor[] getFriendlies(Actor user)
    {
        for(Actor actor : allies) if(actor == user) return allies;
        for(Actor actor : enemies) if(actor == user) return enemies;
        throw new IllegalArgumentException("Actor " + user + " doesn't exist in either the allies' or enemies' arrays.");
    }

}

// obsolete as of now
enum EnemyTypes
{
    GOBLIN(Goblin.class),
    BANDIT(Bandit.class),
    SPAELCASTER(Spaelcaster.class);

    private Class type;

    EnemyTypes(Class c)
    {
        type = c;
    }

    public Actor getType() { try {return (Actor)type.newInstance();} catch(Exception ignored){} return null;}

    static int variants() { return EnemyTypes.values().length; }
}
