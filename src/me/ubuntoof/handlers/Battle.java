package me.ubuntoof.handlers;

import me.ubuntoof.Team;
import me.ubuntoof.characters.*;
import me.ubuntoof.events.globalconditions.GlobalConditionAddEvent;
import me.ubuntoof.events.globalconditions.GlobalConditionRemoveEvent;
import me.ubuntoof.events.globalconditions.GlobalConditionTriggerEvent;
import me.ubuntoof.events.state.*;
import me.ubuntoof.modifiers.Ailment;
import me.ubuntoof.modifiers.GlobalCondition;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.modifiers.globalconditions.Hail;
import me.ubuntoof.utils.Colorizer;
import me.ubuntoof.utils.TextFormatter;

import java.util.*;

public class Battle
{
    public final String battleName;

    public final BattleInteractionsHandler battleInteractionsHandler = new BattleInteractionsHandler();
    private Team[] teams;
    private final List<Actor> combatants = new ArrayList<>();
    private final Map<Actor, Team> incomingActors = new HashMap<>();
    private final Set<GlobalCondition> globalConditions = new HashSet<>();
    public final boolean silent;
    private int turn;

    public Battle(String bn, Team playerTeam, int numEnemies, boolean silent)
    {
        battleName = bn;
        this.silent = silent;
        combatants.addAll(playerTeam.getActors());
        Team enemyTeam = new Team(createEnemies(numEnemies));
        combatants.addAll(enemyTeam.getActors());
        teams = new Team[]{playerTeam, enemyTeam};
    }

    public Battle(String bn, Team[] tms, boolean silent)
    {
        battleName = bn;
        this.silent = silent;
        teams = new Team[tms.length];
        System.arraycopy(tms, 0, teams, 0, tms.length);
    }

    public Battle(String bn, Team[] tms, int otherEnemies, boolean silent)
    {
        battleName = bn;
        this.silent = silent;
        Team enemyTeam = new Team(createEnemies(otherEnemies));

        teams = new Team[tms.length + 1];
        System.arraycopy(tms, 0, teams, 0, tms.length);

        teams[tms.length] = enemyTeam;

        for(Team t : teams) combatants.addAll(t.getActors());
    }


    public void scheduleAddActorToBattle(Actor actor)
    {
        Team newActorTeam = new Team(actor);
        incomingActors.put(actor, newActorTeam);
    }

    private void addTeamToBattle(Team team)
    {
        Team[] newTeams = new Team[teams.length];
        System.arraycopy(teams, 0, newTeams, 0, teams.length);
        newTeams[teams.length] = team;
        teams = newTeams;
    }

    public void scheduleAddActorToBattle(Actor actor, Team team)
    {
        incomingActors.put(actor, team);
    }

    private void handleIncomingCombatants()
    {
        for(Actor actor : incomingActors.keySet())
        {
            Team incomingActorTeam = incomingActors.get(actor);

            boolean matchesAnyTeams = false;
            for(Team t : teams) if (t == incomingActorTeam)
            {
                matchesAnyTeams = true;
                break;
            }
            if(!matchesAnyTeams) addTeamToBattle(incomingActorTeam);

            incomingActorTeam.getActors().add(actor);
            combatants.add(actor);
        }

        List<BattleInteractions> newBattlerInteractionListeners = new ArrayList<>(incomingActors.keySet());
        for(BattleInteractions nbi : newBattlerInteractionListeners) nbi.onEvent(new BattleStartEvent(this));
        battleInteractionsHandler.addAll(newBattlerInteractionListeners);

        incomingActors.clear();
    }

    public void startBattle()
    {
        boolean battleEnded = false;
        for(BattleInteractions bi : combatants) battleInteractionsHandler.add(bi);
        battleInteractionsHandler.registerEvent(new BattleStartEvent(this));

        battleInteractionsHandler.registerEvent(new GlobalConditionAddEvent(this, new Hail()));
        doBriefPause();

        do
        {
            sortActorsBySpeed(combatants, false);
            turn++;
            battleInteractionsHandler.registerEvent(new GlobalTurnStartEvent());

            int playersLeft = 0;
            for(Actor actor : combatants) if(actor instanceof Player && actor.isAlive()) playersLeft++;
            if(playersLeft == 0) displayGlobalBattle();

            for(Actor actor : combatants) if(actor.isAlive())
            {
                println(Colorizer.RESET + "\n" + Colorizer.REVERSE + Colorizer.LIGHT_GRAY + "[" + getCombatantIndex(actor) + "] " + actor.getName() + "'s turn." + Colorizer.RESET);
                battleInteractionsHandler.registerEvent(new TurnChangeEvent(actor));
                for(StatModifier sm : actor.getStatModifiers()) sm.decrementTurnsRemaining();

                doBriefPause();
                battleEnded = (!(checkCombatantSidesAreAlive()));
                if(battleEnded) break;
            }

            println();

            if(!battleEnded)
            {
                for (GlobalCondition gc : globalConditions)
                {
                    battleInteractionsHandler.registerEvent(new GlobalConditionTriggerEvent(this, gc));
                    assert gc.getDurationInTurns() >= 0;
                    if (gc.getDurationInTurns() == 0) battleInteractionsHandler.registerEvent(new GlobalConditionRemoveEvent(this, gc));
                }

                println();

                battleInteractionsHandler.registerEvent(new GlobalTurnEndEvent());
                handleIncomingCombatants();
                doNotablePause();
            }

        } while(checkCombatantSidesAreAlive());

        Team winningTeam = getTeamOf(getLivingCombatants().get(0));
        int teamsLeft = 1;
        for(Actor a : getLivingCombatants())
        {
            Team aTeam = getTeamOf(a);
            if(!(aTeam == winningTeam)) teamsLeft++;
        }

        println(Colorizer.getDivider(80));
        if(teamsLeft == 1)
        {

            announce(Colorizer.RESET + Colorizer.BOLD + Colorizer.YELLOW + "\n\uD83D\uDD31" + Colorizer.RESET +
                    winningTeam.format + " Team " + winningTeam + " emerged victorious!" + Colorizer.RESET);
            for(Actor oa : winningTeam.originalActors) announce(Colorizer.RESET + " - " + oa);

            announce("\n" + Colorizer.getSubdivider(50));

            battleInteractionsHandler.registerEvent(new BattleEndEvent(this, winningTeam));

        } else announce(Colorizer.RED + "Something went wrong; battle ended with " + teamsLeft + " teams remaining.");
        announce(Colorizer.getDivider(80));
    }

    // Selection sorting algorithm which orders actors by speed
    private void sortActorsBySpeed(List<Actor> actorsToSort, boolean isAscending)
    {
        int index; // used to index the 'temporary' Actor before replacement

        for(int i = 0; i < actorsToSort.size() - 1; i++)
        {
            index = i;

            for(int j = i + 1; j < actorsToSort.size(); j++) // retrieves the next element(s)
            {
                if(!isAscending)
                {
                    if (actorsToSort.get(index).getAgility() < actorsToSort.get(j).getAgility()) index = j;
                } else if (actorsToSort.get(index).getAgility() > actorsToSort.get(j).getAgility()) index = j;
            }

            Actor temp = actorsToSort.get(index); // exists for storing the actor whose index is going to be replaced in the selection sort
            actorsToSort.set(index, actorsToSort.get(i));
            actorsToSort.set(i, temp);
        }
    }

    public int getCombatantIndex(Actor actor)
    {
        if(actor == null) throw new IllegalArgumentException();
        for(int i = 0; i < combatants.size(); i++) if(combatants.get(i) == actor) return i;
        throw new NullPointerException("Actor " + actor + " does not currently exist in any team; perhaps they are about to enter the battle (located in " + incomingActors + ")?");
    }

    public void displayGlobalBattle(Actor perspectiveActor)
    {
        println(Colorizer.getDivider(60));
        println(Colorizer.GRAY + "Turn " + turn + " (Displaying by index)" + Colorizer.RESET);

        for(GlobalCondition gc : globalConditions) print(gc.name + " - " + gc.getDurationInTurns() + " turn" + (gc.getDurationInTurns() == 1 ? "" : "s") + " remaining" + Colorizer.RESET);
        println();


        for(int i = 0; i < combatants.size(); i++) if(combatants.get(i).isAlive())
        {
            Actor actor = combatants.get(i);
            Team actorTeam = getTeamOf(actor);
            println((perspectiveActor == actor ? Colorizer.BOLD + "〉" : "  ") + Colorizer.LIGHT_GRAY + "[" + i + "] " + actorTeam + " "
                    + (isActorOnSideOf(perspectiveActor, actor) ? Colorizer.BLUE : Colorizer.RED) + (perspectiveActor == actor ? Colorizer.BOLD : "")
                    + actor.getName() + Colorizer.RESET + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getStamina() + ") "
                    + TextFormatter.formatAsHealthBar("", actor.getHealth(), actor.getStamina(), 15, "▰", "▱")
                    + returnAilmentFormat(actor) + returnStatChangeFormat(actor) + Colorizer.RESET);

        }

        println();
        println(Colorizer.GRAY + "See /stats <target> to display properties of a character." + Colorizer.RESET); // TODO implement commands (i.e. stats)
        println(Colorizer.getDivider(60));
    }

    public void displayGlobalBattle()
    {
        println(Colorizer.getDivider(60));
        println(Colorizer.GRAY + "Turn " + turn + " (Displaying by index)" + Colorizer.RESET);

        for(GlobalCondition gc : globalConditions) println(gc.name + " - " + gc.getDurationInTurns() + " turn" + (gc.getDurationInTurns() == 1 ? "" : "s") + " remaining" + Colorizer.RESET);
        println();


        for(int i = 0; i < combatants.size(); i++) if(combatants.get(i).isAlive())
        {
            Actor actor = combatants.get(i);
            Team actorTeam = getTeamOf(actor);
            println(Colorizer.LIGHT_GRAY + "[" + i + "] " + actorTeam + " "
                    + actor.getName() + Colorizer.RESET + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getStamina() + ") "
                    + TextFormatter.formatAsHealthBar("", actor.getHealth(), actor.getStamina(), 15, "▰", "▱")
                    + returnAilmentFormat(actor) + returnStatChangeFormat(actor) + Colorizer.RESET);

        }

        println(Colorizer.GRAY + "\nSee /stats <target> to display properties of a character." + Colorizer.RESET);
        println(Colorizer.getDivider(60));
    }

    private boolean isActorOnSideOf(Actor perspectiveActor, Actor actor)
    {
        List<Actor> allies = getFriendlies(perspectiveActor);
        for(Actor a : allies) if(actor == a) return true;
        return false;
    }

    private String returnAilmentFormat(Actor actor)
    {
        StringBuilder sb = new StringBuilder();
        for(Ailment ailment : actor.getAilments()) sb.append(ailment.icon);
        return sb.toString() + (sb.toString().length() > 0 ? " " : "");
    }

    private String returnStatChangeFormat(Actor actor)
    {
        StringBuilder sb = new StringBuilder();
        List<StatModifier> smList = actor.getStatModifiers();
        for(StatModifier sm : smList) sb.append(sm).append(" ");
        return sb.toString() + (sb.toString().length() > 0 ? " " : "");
    }

    private boolean checkCombatantSidesAreAlive()
    {
        int teamsAlive = 0;
        for(Team t : teams) if(t.areMembersAlive()) teamsAlive++;
        return teamsAlive > 1;
    }

    private boolean checkIfActorsAlive(Actor[] actors)
    {
        for(Actor actor : actors) if(actor.isAlive()) return true;
        return false;
    }

    public Team generateTeam(int actors, double difficulty)
    {
        return new Team(createEnemies(actors));
    }

    private Actor[] createEnemies(int count)
    {
        Random r = new Random();
        Actor[] enemies = new Actor[count];

        for(int i = 0; i < count; i++)
        {
            enemies[i] = matchEnemyIndex(r.nextInt(4));
        }
        return enemies;
    }

    // Potential source of NPE
    private Actor matchEnemyIndex(int type) {
        Random random = new Random();

        switch (type)
        {
            case 0: return new Bandit(random.nextInt(25));
            case 1: return new Goblin(random.nextInt(25));
            case 2: return new Spaelcaster(random.nextInt(25));
            case 3: return new Druid(random.nextInt(25));
        }
        return null;
    }

    // new Arraylist is returned to prevent clients from adding or removing actors directly to/from the battle
    public List<Actor> getCombatants() { return new ArrayList<>(combatants); }

    public List<Actor> getLivingCombatants()
    {
        List<Actor> livingCombatants = new ArrayList<>();
        for(Actor a : combatants) if(a.isAlive()) livingCombatants.add(a);
        return livingCombatants;
    }

    public List<Actor> getFriendlies(Actor perspectiveActor)
    {
        return getFriendlies(perspectiveActor, false);
    }

    public List<Actor> getFriendlies(Actor perspectiveActor, boolean filterAlive)
    {
        Team perspectiveActorTeam = getTeamOf(perspectiveActor);

        List<Actor> friendlies = new ArrayList<>();
        for (Actor actor : perspectiveActorTeam.getActors()) if(!filterAlive || actor.isAlive()) friendlies.add(actor);
        return friendlies;
    }

    public List<Actor> getOpposition(Actor perspectiveActor)
    {
        return getOpposition(perspectiveActor, false);
    }

    public List<Actor> getOpposition(Actor perspectiveActor, boolean filterAlive)
    {
        Team perspectiveActorTeam = getTeamOf(perspectiveActor);

        List<Actor> enemies = new ArrayList<>();
        for (Actor actor : combatants) if(perspectiveActorTeam != getTeamOf(actor) && (!filterAlive || actor.isAlive())) enemies.add(actor);
        return enemies;
    }

    // Warning - Potential source of NPE
    public Team getTeamOf(Actor actor)
    {
        for(Team t : teams) for(Actor a : t.getActors()) if(a == actor) return t;

        // at this point, the actor isn't apart of any team.
        return null;
    }

    public void announce(String s)
    {
        Colorizer.println(s);
    }
    public void print(String s)
    {
        if(!silent) Colorizer.print(s);
    }
    public void println(String s) { if(!silent) Colorizer.println(s); }
    public void println()
    {
        if(!silent) Colorizer.println();
    }

    public int getTurn() { return turn; }
    private void doBriefPause() { if(!silent) try {Thread.sleep(900); } catch(InterruptedException e) { e.printStackTrace(); } }
    private void doNotablePause() { if(!silent) try {Thread.sleep(2000); } catch(InterruptedException e) { e.printStackTrace(); } }

    public void addGlobalCondition(GlobalCondition gc) { globalConditions.add(gc); }
    public void removeGlobalCondition(GlobalCondition gc) { globalConditions.remove(gc); }
}