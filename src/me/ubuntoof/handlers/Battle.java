package me.ubuntoof.handlers;

import me.ubuntoof.Team;
import me.ubuntoof.characters.*;
import me.ubuntoof.events.globalconditions.GlobalConditionAddEvent;
import me.ubuntoof.events.globalconditions.GlobalConditionRemoveEvent;
import me.ubuntoof.events.globalconditions.GlobalConditionTriggerEvent;
import me.ubuntoof.events.state.*;
import me.ubuntoof.listeners.BattleInteractions;
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
    private int turn;
    private final boolean silent;

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
        for(BattleInteractions nbi : newBattlerInteractionListeners) nbi.notifyEvent(new BattleStartEvent(this));
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
                    if (actorsToSort.get(index).getSpeed() < actorsToSort.get(j).getSpeed()) index = j;
                } else if (actorsToSort.get(index).getSpeed() > actorsToSort.get(j).getSpeed()) index = j;
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
                    + actor.getName() + Colorizer.RESET + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getMaxHealth() + ") "
                    + TextFormatter.formatAsHealthBar("", actor.getHealth(), actor.getBaseMaxHealth(), 10, "▰", "▱") + " "
                    + returnAilmentIcons(actor) + Colorizer.RESET);

        }

        println(Colorizer.GRAY + "\nSee /stats <target> to display properties of a character." + Colorizer.RESET); // TODO implement commands (i.e. stats)
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
                    + actor.getName() + Colorizer.RESET + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getMaxHealth() + ") " +
                    TextFormatter.formatAsHealthBar("", actor.getHealth(), actor.getBaseMaxHealth(), 10, "▰", "▱") + " "
                    + returnAilmentIcons(actor) + Colorizer.RESET);

        }

        println(Colorizer.GRAY + "\nSee /stats <target> to display properties of a character." + Colorizer.RESET);
        println(Colorizer.getDivider(60));
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


    public Actor[] getFriendlies(Actor perspectiveActor)
    {
        Team perspectiveActorTeam = getTeamOf(perspectiveActor);

        Actor[] friendlies = new Actor[perspectiveActorTeam.size()];
        for (int i = 0; i < perspectiveActorTeam.size(); i++) friendlies[i] = perspectiveActorTeam.getActors().get(i);
        return friendlies;
    }

    public Actor[] getOpposition(Actor perspectiveActor)
    {
        Team perspectiveActorTeam = getTeamOf(perspectiveActor);
        int cumulativeSizeOfOpposition = combatants.size() - perspectiveActorTeam.size();
        Actor[] opposition = new Actor[cumulativeSizeOfOpposition];

        int cumulativeIndexesPassed = 0;
        for(Team t : teams) if(t != perspectiveActorTeam)
        {
            for(int i = 0; i < t.size(); i++)
            {
                // require all actors to be added to combatants arraylist before teams
                assert i + cumulativeIndexesPassed <= cumulativeSizeOfOpposition;
                opposition[i + cumulativeIndexesPassed] = t.getActors().get(i);
            }
            cumulativeIndexesPassed += t.size();
        }
        if(opposition.length == 0) print(Colorizer.BOLD + Colorizer.RED + "No enemies of actor " + perspectiveActor + " found; perhaps there were no other teams to begin with?");
        return opposition;
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
        System.out.println(s);
    }
    public void print(String s)
    {
        if(!silent) System.out.print(s);
    }
    public void println(String s) { if(!silent) System.out.println(s); }
    public void println()
    {
        if(!silent) System.out.println();
    }

    public int getTurn() { return turn; }
    private void doBriefPause() { if(!silent) try {Thread.sleep(900); } catch(InterruptedException e) { e.printStackTrace(); } }
    private void doNotablePause() { if(!silent) try {Thread.sleep(2000); } catch(InterruptedException e) { e.printStackTrace(); } }

    public void addGlobalCondition(GlobalCondition gc) { globalConditions.add(gc); }
    public void removeGlobalCondition(GlobalCondition gc) { globalConditions.remove(gc); }
}