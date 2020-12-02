package me.ubuntoof.handlers;

import me.ubuntoof.Party;
import me.ubuntoof.Team;
import me.ubuntoof.WeightedSelector;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.characters.Player;
import me.ubuntoof.entities.*;
import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.globalconditions.GlobalConditionApplicationEvent;
import me.ubuntoof.events.globalconditions.GlobalConditionRemoveEvent;
import me.ubuntoof.events.state.*;
import me.ubuntoof.modifiers.Ailment;
import me.ubuntoof.modifiers.GlobalCondition;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;
import me.ubuntoof.utils.TextFormatter;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Battle
{
    public final Team[] teams;
    private final Party[] parties;
    private final List<Actor> combatants = new ArrayList<>();
    private final Map<Team, Party> teamToPartyMap = new HashMap<>();
    private final Map<Actor, Team> incomingActors = new HashMap<>();
    private final Map<GlobalCondition, Integer> globalConditions = new HashMap<>();

    public final boolean silent; // true when no players are in battle
    private int turn;

    public Battle(Party[] parties)
    {
        this(parties, 0);
    }

    public Battle(Party[] parties, int actorsToGenerate, Actor... otherActors)
    {
        this.parties = parties;

        List<Team> theTeams = new ArrayList<>(createTeamsFromParties(parties));
        if(actorsToGenerate > 0) theTeams.add(generateTeam(actorsToGenerate));
        for(Actor a : otherActors)
            theTeams.add(new Team(a));

        teams = new Team[theTeams.size()];
        for(int i = 0; i < teams.length; i++) teams[i] = theTeams.get(i);

        for(Team t : teams)
            combatants.addAll(t.getActors());

        boolean isPlayerInBattle = false;
        for(Actor a : combatants) if (a instanceof Player) { isPlayerInBattle = true; break; }
        silent = false; //!isPlayerInBattle;
    }

    /**
     * Adds an actor to a list that is added to the combatants list at the end of the turn.
     * Nothing will be done if the actor's team does not match any of those currently in the battle.
     * @param e The entity to create an Actor from and add to queue
     * @param t The team to add the actor to
     * @implNote The Actor being added will not persist across Battles where the same Team is used.
     */
    public void scheduleAddEntityToBattle(Entity e, Team t)
    {
       for(Team team : teams) if(team == t) { incomingActors.put(createActorFromEntity(e), t); break; }
    }

    public void scheduleAddEntityToBattle(Entity... entities)
    {
        for(Entity e : entities) scheduleAddEntityToBattle(e, new Team(createActorFromEntity(e)));
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
            if(!matchesAnyTeams) throw new NullPointerException("All actors should be given a team when entering the battle."); // addTeamToBattle(incomingActorTeam);

            incomingActorTeam.add(actor);
            combatants.add(actor);
            actor.onEvent(new BattleStartEvent());
        }

        incomingActors.clear();
    }

    // TODO refactor into smaller pieces to use for inheritance
    public Party run()
    {
        boolean battleEnded = false;
        registerEvent(new BattleStartEvent());

        doBriefPause();

        while(checkOpposingCombatantSidesAreAlive())
        {
            sortActorsByAgility(combatants, false);
            turn++;
            registerEvent(new GlobalTurnStartEvent());

            int playersLeft = 0;
            for(Actor actor : combatants) if(actor instanceof Player && actor.isAlive()) playersLeft++;
            if(playersLeft == 0) displayScenario();

            for(Actor actor : combatants) if(actor.isAlive())
            {
                println(Colorizer.RESET + "\n" + Colorizer.REVERSE + Colorizer.LIGHT_GRAY + "[" + getCombatantIndex(actor) + "] " + actor.name + "'s turn." + Colorizer.RESET);
                registerEvent(new TurnChangeEvent(actor));

                doBriefPause();
                battleEnded = (!(checkOpposingCombatantSidesAreAlive()));
                if(battleEnded) break;
            }

            println();

            if(!battleEnded)
            {
                for (Map.Entry<GlobalCondition, Integer> entry : globalConditions.entrySet())
                {
                    GlobalCondition gc = entry.getKey();
                    for (Actor a : getLivingCombatants()) registerEvent(new GlobalConditionApplicationEvent(gc, a));
                    if (entry.getValue() == 0) registerEvent(new GlobalConditionRemoveEvent(this, gc));
                }

                println();
                registerEvent(new GlobalTurnEndEvent());
                doNotablePause();
                handleIncomingCombatants();
            }
        }

        Team winningTeam = getLivingCombatants().get(0).getTeam();

        int teamsLeft = 1;
        for(Actor a : getLivingCombatants())
        {
            Team aTeam = a.getTeam();
            if(aTeam != winningTeam) teamsLeft++;
        }

        if(teamsLeft == 1)
        {
            announce(Colorizer.RESET + Colorizer.BOLD + Colorizer.YELLOW + "\n\uD83D\uDD31" + Colorizer.RESET +
                    winningTeam.format + " Team " + winningTeam + " emerged victorious!" + Colorizer.RESET);
            for(Actor oa : winningTeam.originalActors) announce(Colorizer.RESET + " - " + oa);

            announce("\n" + Colorizer.getSubdivider(50));

            registerEvent(new BattleEndEvent(this, winningTeam));

        } else throw new IllegalStateException("Something went wrong; battle ended with " + teamsLeft + " teams remaining.");
        announce(Colorizer.getDivider(80));

        return teamToPartyMap.get(winningTeam);
    }

    /**
     * Performs a selection sort on combatant Actors by speed.
     */
    private void sortActorsByAgility(List<Actor> actorsToSort, boolean isAscending)
    {
        int tIndex; // used to index the 'temporary' Actor before replacement
        for(int i = 0; i < actorsToSort.size() - 1; i++)
        {
            tIndex = i;
            for(int j = i + 1; j < actorsToSort.size(); j++)
                if(isAscending ? actorsToSort.get(tIndex).getAgility() > actorsToSort.get(j).getAgility() : actorsToSort.get(tIndex).getAgility() < actorsToSort.get(j).getAgility()) tIndex = j;
            Actor temp = actorsToSort.get(tIndex); // exists for storing the actor whose index is going to be replaced in the selection sort
            actorsToSort.set(tIndex, actorsToSort.get(i));
            actorsToSort.set(i, temp);
        }
    }

    /**
     * @param actor The object reference to search for; will fail if null.
     * @return The index of the actor's position in the {@code combatants} List.
     */
    public int getCombatantIndex(Actor actor)
    {
        for(int i = 0; i < combatants.size(); i++) if(combatants.get(i) == actor) return i;
        throw new NullPointerException("Actor " + actor + " does not currently exist in any team; perhaps they are about to enter the battle (located in " + incomingActors + ")?");
    }

    public void displayScenario(Actor perspectiveActor)
    {
        println(Colorizer.getDivider(60));
        println(Colorizer.GRAY + "Turn " + turn + " (Displaying by index)" + Colorizer.RESET);

        for (Map.Entry<GlobalCondition, Integer> entry : globalConditions.entrySet())
        {
            GlobalCondition gc = entry.getKey();
            int turnsRemaining = entry.getValue();
            print(gc.name + " - " + turnsRemaining + " turn" + (turnsRemaining == 1 ? "" : "s") + " remaining" + Colorizer.RESET);
        }


        println("\n");

        for(int i = 0; i < combatants.size(); i++) if(combatants.get(i).isAlive())
        {
            Actor actor = combatants.get(i);
            Team actorTeam = actor.getTeam();
            println((perspectiveActor == actor ? Colorizer.BOLD + "〉" : "  ") + Colorizer.LIGHT_GRAY + "[" + i + "] " + actorTeam + " "
                    + (isActorOnSideOf(perspectiveActor, actor) ? Colorizer.BLUE : Colorizer.RED) + (perspectiveActor == actor ? Colorizer.BOLD : "")
                    + actor.name + Colorizer.RESET + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getStamina() + ") "
                    + TextFormatter.formatAsHealthBar("", actor.getHealth(), actor.getStamina(), 15, "▰", "▱")
                    + returnAilmentFormat(actor) + returnStatChangeFormat(actor) + Colorizer.RESET);

        }

        println();
        println(Colorizer.GRAY + "See /stats <target> to display properties of a character." + Colorizer.RESET); // TODO implement commands (i.e. stats)
        println(Colorizer.getDivider(60));
    }

    public void displayScenario()
    {
        println(Colorizer.getDivider(60));
        println(Colorizer.GRAY + "Turn " + turn + " (Displaying by index)" + Colorizer.RESET);

        for(Map.Entry<GlobalCondition, Integer> entry : globalConditions.entrySet())
        {
            int turnsRemaining = entry.getValue();
            println(entry.getKey().name + " - " + turnsRemaining + " turn" + (turnsRemaining == 1 ? "" : "s") + " remaining" + Colorizer.RESET);
        }
        println("\n");


        for(int i = 0; i < combatants.size(); i++) if(combatants.get(i).isAlive())
        {
            Actor actor = combatants.get(i);
            Team actorTeam = actor.getTeam();
            println(Colorizer.LIGHT_GRAY + "[" + i + "] " + actorTeam + " "
                    + actor.name + Colorizer.RESET + Colorizer.LIGHT_GRAY + " (" + actor.getHealth() + "/" + actor.getStamina() + ") "
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
        for(Ailment ailment : actor.getAilments()) sb.append(ailment).append(' '); // sb.append(ailment.icon);
        return sb.toString() + (sb.toString().length() > 0 ? " " : "");
    }

    private String returnStatChangeFormat(Actor actor)
    {
        StringBuilder sb = new StringBuilder();
        Set<StatModifier> smSet = actor.getStatModifiers();
        for(StatModifier sm : smSet) sb.append(sm).append(" ");
        return sb.toString() + (sb.toString().length() > 0 ? " " : "");
    }

    /**
     * @return A boolean indicating whether or not more than one team remains in the battle.
     */
    private boolean checkOpposingCombatantSidesAreAlive()
    {
        int teamsAlive = 0;
        for(Team t : teams) if(t.areAnyMembersAlive()) teamsAlive++;
        return teamsAlive > 1;
    }

    public Team generateTeam(int actors)
    {
        return new Team(generateEnemies(actors));
    }

    public Actor[] generateEnemies(int count)
    {
        Actor[] enemies = new Actor[count];
        for(int i = 0; i < count; i++) enemies[i] = generateEnemy();
        return enemies;
    }

    public Actor generateEnemy()
    {
        WeightedSelector<Class<? extends Entity>> weightedSelector = new WeightedSelector<>();
        weightedSelector.put(Bandit.class, 1)
                .put(Druid.class, .65)
                .put(Goblin.class, .9)
                .put(Golem.class, .45)
                .put(Spaelcaster.class, .7);
        try
        {
            Class<? extends Entity> clazz = weightedSelector.getRandom();
            return createActorFromEntity(clazz.getConstructor(int.class).newInstance(5));
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) { e.printStackTrace(); }
        return null;
    }

    // new Arraylist is returned to prevent clients from adding or removing actors directly to/from the battle
    /**
    * @return A copy of the {@code combatants} list.
    */
    public List<Actor> getCombatants() { return new ArrayList<>(combatants); }

    /**
     * @return A list of all living combatants in the {@code combatants} list.
     */
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
        Team perspectiveActorTeam = perspectiveActor.getTeam();

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
        Team perspectiveActorTeam = perspectiveActor.getTeam();
        List<Actor> enemies = new ArrayList<>();
        for (Actor actor : combatants) if(perspectiveActorTeam != actor.getTeam() && (!filterAlive || actor.isAlive())) enemies.add(actor);
        return enemies;
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
    private void doBriefPause() { if(!silent) try {Thread.sleep(900); } catch(InterruptedException ignored){} }
    private void doNotablePause() { if(!silent) try {Thread.sleep(2000); } catch(InterruptedException ignored){} }

    public void addGlobalCondition(GlobalCondition gc, int turns) { globalConditions.put(gc, turns); }
    public void removeGlobalCondition(GlobalCondition gc) { globalConditions.remove(gc); }

    public void registerEvent(Event e)
    {
        for(Actor a : combatants) a.onEvent(e);
        for(GlobalCondition gc : globalConditions.keySet()) gc.onEvent(e);
        if(e instanceof Cancellable && (((Cancellable) e).isDisallowed())) return;
        e.perform();
    }

    private Actor createActorFromEntity(Entity e)
    {
        return e.isPlayerControlled() ? new Player(this, e) : new Actor(this, e);
    }

    private Actor[] createActorsFromEntities(List<Entity> es)
    {
        Actor[] res = new Actor[es.size()];
        for(int i = 0; i < res.length; i++) res[i] = createActorFromEntity(es.get(i));
        return res;
    }

    private List<Team> createTeamsFromParties(Party... ps)
    {
        List<Team> res = new ArrayList<>();
        for(Party p : ps)
        {
            Team t = new Team(createActorsFromEntities(p.entities));
            teamToPartyMap.put(t, p);
            res.add(t);
        }
        return res;
    }
}