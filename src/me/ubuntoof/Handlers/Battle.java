package me.ubuntoof.Handlers;

import com.sun.istack.internal.NotNull;
import me.ubuntoof.Characters.*;
import me.ubuntoof.Listeners.*;
import me.ubuntoof.Modifiers.*;
import me.ubuntoof.Modifiers.GlobalConditions.Hail;
import me.ubuntoof.Team;
import me.ubuntoof.Utils.*;

import java.util.*;

public class Battle {

    private Set<GlobalCondition> globalConditions = new HashSet<>();
    private Team[] teams;
    private ArrayList<Actor> combatants = new ArrayList<>();
    private Map<Actor, Team> incomingActors = new HashMap<>();

    private List<BattleInteractions> battleInteractionsListeners = new ArrayList<>();
    private int turn;

    public Battle(Team playerTeam, int numEnemies)
    {
        combatants.addAll(playerTeam.getActors());
        Team enemyTeam = new Team(createEnemies(numEnemies));
        combatants.addAll(enemyTeam.getActors());
        teams = new Team[]{playerTeam, enemyTeam};
    }

    public Battle(Team[] tms)
    {
        teams = new Team[tms.length];
        System.arraycopy(tms, 0, teams, 0, tms.length);
    }

    public Battle(Team[] tms, int otherEnemies)
    {
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

        //combatants.addAll(incomingActors.keySet()); // add queued actors into the battle; now they can be targeted.

        ArrayList<BattleInteractions> newBattlerInteractionListeners = new ArrayList<>(incomingActors.keySet());
        for(BattleInteractions nbi : newBattlerInteractionListeners) nbi.onBattleStarted(this);
        battleInteractionsListeners.addAll(newBattlerInteractionListeners);

        incomingActors.clear();
    }

    public void startBattle()
    {
        boolean battleEnded = false;
        battleInteractionsListeners.addAll(combatants);
        globalConditions.add(new Hail());

        // run through original combatant listeners
        for(BattleInteractions bi : battleInteractionsListeners) bi.onBattleStarted(this);

        do
        {

            sortActorsBySpeed(combatants, false);
            turn++;
            for(BattleInteractions bi : battleInteractionsListeners) bi.onGlobalTurnStarted();

            for(Actor actor : combatants) if(actor.isAlive())
            {

                System.out.println(Colorizer.RESET + "\n" + Colorizer.REVERSE + Colorizer.LIGHT_GRAY + "[" + getCombatantIndex(actor) + "] " + actor.getName() + "'s turn." + Colorizer.RESET);
                for(StatModifier sm : actor.getStatModifiers()) sm.decrementTurnsRemaining();
                actor.onActorTurn();
                doBriefPause();
                for(BattleInteractions bi : battleInteractionsListeners) bi.onTurnChanged();
                battleEnded = (!(checkCombatantSidesAreAlive()));
                if(battleEnded) break;
            }

            System.out.println();

            if(!battleEnded) {

                // used for global conditions
                Actor[] tempCombatantsArray = new Actor[combatants.size()];
                for(int i = 0; i < combatants.size(); i++) tempCombatantsArray[i] = combatants.get(i);

                for (GlobalCondition gc : globalConditions) {
                    gc.applyEffects(tempCombatantsArray);
                    assert gc.getDurationInTurns() >= 0;
                    if (gc.getDurationInTurns() == 0) globalConditions.remove(gc);
                }

                doNotablePause();
                System.out.println();

                for (BattleInteractions bi : battleInteractionsListeners) bi.onGlobalTurnEnded();
                handleIncomingCombatants();
            }

        } while(checkCombatantSidesAreAlive());

        for(BattleInteractions bi : battleInteractionsListeners) bi.onBattleEnded();

        Team winningTeam = getTeamOf(getLivingCombatants().get(0));
        int teamsLeft = 1;
        for(Actor a : getLivingCombatants())
        {
            Team aTeam = getTeamOf(a);
            if(!(aTeam == winningTeam)) teamsLeft++;
        }

        Colorizer.printDivider(60);
        if(teamsLeft == 1)
        {
            System.out.println(Colorizer.RESET + Colorizer.BOLD + Colorizer.YELLOW + "\n\uD83D\uDD31" + Colorizer.RESET +
                    winningTeam.format + " Team " + winningTeam + " emerged victorious!" + Colorizer.RESET);
            for(Actor oa : winningTeam.originalActors) System.out.println(Colorizer.RESET + " - " + oa);
        } else System.out.println(Colorizer.RED + "Something went wrong; battle ended with " + teamsLeft + " teams remaining.");
        Colorizer.printDivider(60);
    }

    // Selection sorting algo which orders actors by speed
    private void sortActorsBySpeed(ArrayList<Actor> actorsToSort, boolean isAscending)
    {
        int index; // used to index the 'temporary' Actor before replacement

        for(int i = 0; i < actorsToSort.size() - 1; i++)
        {
            index = i;

            for(int j = i + 1; j < actorsToSort.size(); j++) // retrieves the next element(s)
            {
                if(isAscending)
                {
                    if (actorsToSort.get(i).getSpeed() > actorsToSort.get(j).getSpeed()) index = j;
                } else if (actorsToSort.get(i).getSpeed() < actorsToSort.get(j).getSpeed()) index = j;

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
        Colorizer.printDivider(60);
        System.out.println(Colorizer.GRAY + "Turn " + turn + " (Displaying by index)" + Colorizer.RESET);

        for(GlobalCondition gc : globalConditions) System.out.println(gc.name + " - " + gc.getDurationInTurns() + " turn" + (gc.getDurationInTurns() == 1 ? "" : "s") + " remaining" + Colorizer.RESET);
        System.out.println();


        for(int i = 0; i < combatants.size(); i++) if(combatants.get(i).isAlive())
        {
            Actor actor = combatants.get(i);
            Team actorTeam = getTeamOf(actor);
            System.out.println((perspectiveActor == actor ? Colorizer.BOLD + "〉" : "  ") + Colorizer.LIGHT_GRAY + "[" + i + "] " + actorTeam + " "
                    + (isActorOnSideOf(perspectiveActor, actor) ? Colorizer.BLUE : Colorizer.RED) + (perspectiveActor == actor ? Colorizer.BOLD : "")
                    + actor.getName() + Colorizer.RESET + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getMaxHealth() + ") "
                    + TextFormatter.formatAsHealthBar("", actor.getHealth(), actor.getBaseMaxHealth(), 10, "▰", "▱") + " "
                    + returnAilmentIcons(actor) + Colorizer.RESET);

        }

        System.out.println(Colorizer.GRAY + "\nSee /stats <target> to display properties of a character." + Colorizer.RESET); // TODO implement commands (i.e. stats)
        Colorizer.printDivider(60);
    }

    public void displayGlobalBattle()
    {
        Colorizer.printDivider(60);
        System.out.println(Colorizer.GRAY + "Turn " + turn + " (Displaying by index)" + Colorizer.RESET);

        for(GlobalCondition gc : globalConditions) System.out.println(gc.name + " - " + gc.getDurationInTurns() + " turn" + (gc.getDurationInTurns() == 1 ? "" : "s") + " remaining" + Colorizer.RESET);
        System.out.println();


        for(int i = 0; i < combatants.size(); i++) if(combatants.get(i).isAlive())
        {
            Actor actor = combatants.get(i);
            Team actorTeam = getTeamOf(actor);
            System.out.println(Colorizer.LIGHT_GRAY + "[" + i + "] " + actorTeam + " "
                    + actor.getName() + Colorizer.RESET + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getMaxHealth() + ") " +
                    TextFormatter.formatAsHealthBar("", actor.getHealth(), actor.getBaseMaxHealth(), 10, "▰", "▱") + " "
                    + returnAilmentIcons(actor) + Colorizer.RESET);

        }

        System.out.println(Colorizer.GRAY + "\nSee /stats <target> to display properties of a character." + Colorizer.RESET);
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
        int teamsAlive = 0;
        for(Team t : teams) if(t.areMembersAlive()) teamsAlive++;
        return teamsAlive > 1;
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
            enemies[i] = matchEnemyIndex(r.nextInt(4)); // EnemyTypes.variants
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
    public ArrayList<Actor> getCombatants() { return new ArrayList<>(combatants); }
    // TODO figure out if this is needed
    public ArrayList<Actor> getLivingCombatants()
    {
        ArrayList<Actor> livingCombatants = new ArrayList<>();
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
                opposition[i + cumulativeIndexesPassed] = t.getActors().get(i); // TODO fix
            }
            cumulativeIndexesPassed += t.size();
        }
        if(opposition.length == 0) System.out.println(Colorizer.BOLD + Colorizer.RED + "No enemies of actor " + perspectiveActor + " found; perhaps there were no other teams to begin with?");
        return opposition;
    }

    // Warning - Potential source of NPE
    public Team getTeamOf(Actor actor)
    {
        for(Team t : teams) for(Actor a : t.getActors()) if(a == actor) return t;

        // at this point, the actor isn't apart of any team.
        return null;
    }

    public int getTurn() { return turn; }
    private void doBriefPause()
    {
        try {Thread.sleep(900); } catch(InterruptedException e) { e.printStackTrace(); }
    }
    private void doNotablePause() { try {Thread.sleep(2000); } catch(InterruptedException e) { e.printStackTrace(); } }
}

// obsolete
enum EnemyTypes
{
    GOBLIN(Goblin.class),
    BANDIT(Bandit.class),
    SPAELCASTER(Spaelcaster.class),
    DRUID(Druid.class);

    private Class type;

    EnemyTypes(Class c)
    {
        type = c;
    }

    public Actor getType() { try {return (Actor)type.newInstance();} catch(Exception ignored){} return null;}

    // only getter which is used within the enum
    static int variants() { return EnemyTypes.values().length; }

}
