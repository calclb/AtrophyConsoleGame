package me.ubuntoof.Handlers;

import me.ubuntoof.Characters.*;
import me.ubuntoof.Listeners.*;
import me.ubuntoof.Modifiers.*;
import me.ubuntoof.Modifiers.GlobalConditions.Hail;
import me.ubuntoof.Utils.*;

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
        boolean battleEnded = false;
        battleInteractionsListeners.addAll(Arrays.asList(combatants));

        globalConditions.add(new Hail());

        // run through combatant listeners every turn
        for(BattleInteractions bi : battleInteractionsListeners) bi.onBattleStarted(this);

        do
        {
            sortActorsBySpeed(combatants, false);
            turn++;
            for(Actor actor : combatants) if(actor instanceof Player) displayGlobalBattle(actor);
            for(BattleInteractions bi : battleInteractionsListeners) bi.onGlobalTurnStarted();

            Colorizer.clear();
            for(Actor actor : combatants) if(actor.isAlive())
            {
                System.out.println(Colorizer.RESET + "\n" + Colorizer.REVERSE + Colorizer.LIGHT_GRAY + "[" + getCombatantIndex(actor) + "] " + actor.getName() + "'s turn." + Colorizer.RESET);
                for(StatModifier sm : actor.getStatModifiers()) sm.decrementTurnsRemaining();
                actor.onActorTurn();
                try {Thread.sleep(900); } catch(InterruptedException e) { e.printStackTrace(); }
                for(BattleInteractions bi : battleInteractionsListeners) bi.onTurnChanged();
                battleEnded = (!(checkIfActorsAlive(allies) && checkIfActorsAlive(enemies)));
                if(battleEnded) break;
            }

            System.out.println();

            if(!battleEnded) {
                for (GlobalCondition gc : globalConditions) {
                    gc.applyEffects(combatants);
                    assert gc.getDurationInTurns() >= 0;
                    if (gc.getDurationInTurns() == 0) globalConditions.remove(gc);
                }

                System.out.println();

                for (BattleInteractions bi : battleInteractionsListeners) bi.onGlobalTurnEnded();
            }

        } while(checkCombatantSidesAreAlive());

        for(BattleInteractions bi : battleInteractionsListeners) bi.onBattleEnded();
        if(checkIfActorsAlive(allies)) System.out.println(Colorizer.BOLD + Colorizer.GREEN + "Hooray! We won!"); else System.out.println(Colorizer.BOLD + Colorizer.RED + "Oh noes, we lost.");
    }

    private void sortActorsBySpeed(Actor[] actorsToSort, boolean isAscending)
    {
        int index; // used to index the 'temporary' Actor before replacement

        for(int i = 0; i < actorsToSort.length - 1; i++)
        {
            index = i;

            for(int j = i + 1; j < actorsToSort.length; j++) // retrieves the next element(s)
            {
                if(isAscending)
                {
                    if (actorsToSort[i].getSpeed() > actorsToSort[j].getSpeed()) index = j;
                } else if (actorsToSort[i].getSpeed() < actorsToSort[j].getSpeed()) index = j;

            }

            Actor temp = actorsToSort[index]; // exists for storing the actor whose index is going to be replaced in the selection sort
            actorsToSort[index] = actorsToSort[i];
            actorsToSort[i] = temp;
        }
    }

    public int getCombatantIndex(Actor actor)
    {
        if(actor == null) throw new IllegalArgumentException();
        for(int i = 0; i < combatants.length; i++) if(combatants[i] == actor) return i;
        throw new NullPointerException();
    }

    private void displayGlobalBattle(Actor perspectiveActor)
    {
        Colorizer.printDivider(60);
        System.out.println(Colorizer.GRAY + "Turn " + turn + " (Displaying by index)" + Colorizer.RESET);

        for(GlobalCondition gc : globalConditions) System.out.println(gc.name + " - " + gc.getDurationInTurns() + " turn" + (gc.getDurationInTurns() == 1 ? "" : "s") + " remaining" + Colorizer.RESET);
        System.out.println();


        for(int i = 0; i < combatants.length; i++) if(combatants[i].isAlive())
        {
            Actor actor = combatants[i];
            System.out.println(Colorizer.LIGHT_GRAY + "[" + i + "] " + (isActorOnSideOf(perspectiveActor, actor) ? Colorizer.BLUE : Colorizer.RED) + (perspectiveActor == actor ? Colorizer.BOLD : "")
                    + actor.getName() + Colorizer.RESET + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getMaxHealth() + ") " +
                    TextFormatter.formatAsProgressBar("", actor.getHealth(), actor.getMaxHealth(), 10, Colorizer.GREEN + "▰", Colorizer.LIGHT_RED + "▱") + " "
                    + returnAilmentIcons(actor) + Colorizer.RESET);

        }

        /*for(int i = 0; i < allies.length; i++) if(allies[i].isAlive())
        {
            Actor actor = allies[i];
            System.out.println(Colorizer.LIGHT_GRAY + "[" + i + "] " + Colorizer.BLUE + actor.getName() + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getMaxHealth() + ") " +
                    TextFormatter.formatAsProgressBar("", actor.getHealth(), actor.getMaxHealth(), 10, Colorizer.GREEN + "▰", Colorizer.LIGHT_RED + "▱") + " " + returnAilmentIcons(actor) + Colorizer.RESET);
        }

        for(int i = 0; i < enemies.length; i++) if(enemies[i].isAlive())
        {
            Actor actor = enemies[i];
            System.out.println(Colorizer.LIGHT_GRAY + "[" + (i + allies.length) + "] " + Colorizer.RED + actor.getName() + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getMaxHealth() + ") " +
                    TextFormatter.formatAsProgressBar("", actor.getHealth(), actor.getMaxHealth(), 10, Colorizer.GREEN + "▰", Colorizer.LIGHT_RED + "▱") + " " + returnAilmentIcons(actor) + Colorizer.RESET);
        }*/

        System.out.println(Colorizer.GRAY + "\nSee /stats <target> to display properties of a character." + Colorizer.RESET); // TODO implement commands (i.e. stats)
        Colorizer.printDivider(60);
    }

    private boolean isActorOnSideOf(Actor perspectiveActor, Actor actor)
    {
        Actor[] allies = getFriendlies(perspectiveActor);
        for(Actor a : allies) if(actor == a) return true;
        return false;
    }

    private String returnAilmentIcons(Actor actor) {
        StringBuilder sb = new StringBuilder();
        for(Ailment ailment : actor.getAilments()) sb.append(ailment.icon);
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

    private boolean checkIfActorsAlive(Actor[] actors)
    {
        for(Actor actor : actors) if(actor.isAlive()) return true;
        return false;
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
            case 0: return new Bandit(random.nextInt(25));
            case 1: return new Goblin(random.nextInt(25));
            case 2: return new Spaelcaster(random.nextInt(25));
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

    public int getTurn() { return turn; }

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
