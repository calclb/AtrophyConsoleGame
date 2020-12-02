package me.ubuntoof.characters;

import me.ubuntoof.Stats;
import me.ubuntoof.Team;
import me.ubuntoof.WeightedSelector;
import me.ubuntoof.actions.Action;
import me.ubuntoof.entities.Entity;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.actors.ActionCommitEvent;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.events.actors.ActorDeathEvent;
import me.ubuntoof.events.ailments.AilmentAddEvent;
import me.ubuntoof.events.ailments.AilmentRemoveEvent;
import me.ubuntoof.events.ailments.AilmentTriggerEvent;
import me.ubuntoof.events.state.BattleEndEvent;
import me.ubuntoof.events.state.BattleStartEvent;
import me.ubuntoof.events.state.GlobalTurnEndEvent;
import me.ubuntoof.events.state.TurnChangeEvent;
import me.ubuntoof.events.statmodifiers.StatModifierAddEvent;
import me.ubuntoof.events.statmodifiers.StatModifierRemoveEvent;
import me.ubuntoof.handlers.Battle;
import me.ubuntoof.handlers.BattleInteractions;
import me.ubuntoof.modifiers.Ailment;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.passives.Condition;
import me.ubuntoof.utils.Colorizer;
import me.ubuntoof.utils.TextFormatter;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Actors represent the combatants of a battle. They are meant to be only used within one battle, and not in others.
 * If parts of an actor (e.g. level or name) are to be kept across battles,
 * @see Entity
 */
public class Actor implements BattleInteractions
{
    // region - Permanent references & values
    public final Battle battle;
    public final Entity entity;

    protected final Random random = new Random();
    public final String name;
    public final int level;
    public final int expValue;
    // endregion

    // region - Stats (immutable); stat calculations are done with StatModifiers in mind.
    public final int baseStamina;
    public final int baseStrength;
    public final int baseAgility;
    // endregion

    // region - Battle-prone values
    private final List<Action> actions = new ArrayList<>();

    private final Set<StatModifier> statModifiers = new HashSet<>();
    private final Set<Condition> conditions = new HashSet<>();
    private final Map<Action, Integer> actionCooldowns = new HashMap<>();

    private int health;
    private boolean eligibleToAct = true;
    private boolean eliminated = false;
    private int exp = 0;
    // endregion

    /**
     * Should only be called by the {@code Battle} class.
     * @see Battle
     */
    public Actor(Battle b, Entity en)
    {
        battle = b;
        entity = en;

        for(Class<? extends Action> uAction : en.getActions())
        {
            try
            {
                actions.add(uAction.getConstructor().newInstance());
            }
            catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) { e.printStackTrace(); }
        }

        for(Class<? extends Condition> uPassive : en.getConditions())
        {
            try
            {
                conditions.add(uPassive.getConstructor().newInstance());
            }
            catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) { e.printStackTrace(); }
        }


        baseStamina = en.getBaseStamina();
        health = baseStamina;
        baseStrength = en.getBaseStrength();
        baseAgility = en.getBaseAgility();

        name = en.getName();
        level = Math.min(Math.max(en.getLevel(), 1), Entity.MAX_LEVEL);
        expValue = level/2;
    }

    /**
     * @return The exp gained from actors of other teams in battle.
     */
    protected int getExp() { return exp; }

    /**
     * @return The {@code actions} List. Mutating the state of the List will be reflected in this object.
     */
    public List<Action> getActions()
    {
        return actions;
    }

    public List<Action> getUsableActions()
    {
        List<Action> res = new ArrayList<>();
        for(Action a : actions) if(!actionCooldowns.containsKey(a)) res.add(a);
        return res;
    }

    /**
     * @return The {@code passives} List. Mutating the state of the List will be reflected in this object.
     */
    public Set<Condition> getConditions() { return conditions; }

    public void addCondition(Condition c) { conditions.add(c); }

    // going to transition to the upper method
    public void doAction(Action action, Actor primaryTarget)
    {
        battle.registerEvent(new ActionCommitEvent(this, action, primaryTarget));
    }

    public void addActionCooldown(Action a, int turns) { actionCooldowns.put(a, turns); }
    public void removeActionCooldown(Action a) { actionCooldowns.remove(a); }
    public int getActionCooldown(Action a)
    {
        return actionCooldowns.getOrDefault(a, 0);
    }

    public boolean getEligibleToAct() { return eligibleToAct; }
    public void setEligibleToAct(boolean to) { eligibleToAct = to; }

    /**
     * This should normally only be invoked by events, as it does not recursively trigger events. Use the {@code takeDamage()} method to make an {@code Actor} take damage, as this method
     * is called by events to avoid a {@code StackOverflowError}.
     * @see StackOverflowError
     */
    public void takeRawDamage(int dmg, boolean trueDamage)
    {
        health = Math.max(health - dmg, 0);
        performEliminationCheck();
    }

    // Damage-related methods
    public ActorDamageEvent takeDamage(int dmg)
    {
        //if(dmg < getArmor()) dmg = 0;
        //else dmg = Math.max(dmg - getDefense(), 0);

        ActorDamageEvent ade = new ActorDamageEvent(this, dmg, false);
        battle.registerEvent(ade);
        return ade;
    }

    public ActorDamageEvent takeDamage(int dmg, boolean trueDamage)
    {
        ActorDamageEvent ade = new ActorDamageEvent(this, dmg, trueDamage);
        battle.registerEvent(ade);
        return ade;
    }

    // Stat-related methods

    /**
     * @return The stamina of the Actor, affected by active StatModifiers.
     */
    public int getStamina()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.STAMINA) mod *= sm.getMultiplier();
        return (int)(mod * baseStamina);
    }

    /**
     * @return The remaining health of the Actor.
     */
    public int getHealth() { return health; }

    /**
     * Sets the health of an Actor.
     */
    public void setHealth(int h) { health = h; }

    /**
     * @return The strength of the Actor, affected by active StatModifiers.
     */
    public int getStrength()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.STRENGTH) mod *= sm.getMultiplier();
        return (int)(mod * baseStrength);
    }

    /**
     * @return The agility of the Actor, affected by active StatModifiers.
     */
    public int getAgility()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.AGILITY) mod *= sm.getMultiplier();
        return (int)(mod * baseAgility);
    }

    // State and modifier-related methods
    /**
     * @return A boolean indicating whether or not an Actor is alive (i.e. where {@code health > 0d}).
     */
    public boolean isAlive() { return health > 0d; }

    /**
     * @return A copy of a Set of the Actor's current ailments. Keep in mind that the references of the original ailments
     * Use {@code addAilment()} to add an {@code Ailment}.
     */
    public Set<Ailment> getAilments()
    {
        Set<Ailment> res = new HashSet<>();
        for(Condition c : conditions) if(c instanceof Ailment) res.add((Ailment)c);
        return res;
    }
    public void addAilment(Ailment ailment)
    {
        battle.registerEvent(new AilmentAddEvent(this, ailment)); // it.remove();
    }

    /**
     * This method can be used to get or remove a {@code StatModifier}.
     * Use {@code addStatModifier()} to add a {@code StatModifier}.
     */
    public Set<StatModifier> getStatModifiers() { return statModifiers; }
    public void addStatModifier(StatModifier sm)
    {
        battle.registerEvent(new StatModifierAddEvent(this, sm));
    }

    /**
     * Allows an Actor to specifically react to an Event.
     */
    @Override public void onEvent(Event e)
    {
        if(e instanceof BattleStartEvent) onBattleStart();
        if(e instanceof TurnChangeEvent) onTurnChanged();
        if(e instanceof GlobalTurnEndEvent) onGlobalTurnEnded();
        if(e instanceof BattleEndEvent) onBattleEnd();
        if(e instanceof ActorDeathEvent)
        {
            ActorDeathEvent ade = (ActorDeathEvent) e;
            if(ade.actor.getTeam() != getTeam()) exp += ade.actor.expValue;
        }
        for(Condition c : conditions) c.on(this, e);
    }

    /**
     * Called when the battle begins. The {@code battle} reference held in this object should be {@code null}, unless a solution is reached where battles can be
     * more seamlessly passed in the constructor of the object.
     * @see Battle
     */
    public void onBattleStart()
    {
    }

    /**
     * Called when the battle ends.
     */
    public void onBattleEnd()
    {
        actionCooldowns.clear();
        statModifiers.clear();

        actions.clear();
        conditions.clear();

        health = baseStamina;
    }

    /**
     * Called when it is another Actor's turn (including this one).
     */
    public void onTurnChanged()
    {
        performEliminationCheck();
    }

    /**
     * Called when all Actors are done with their turns.
     */
    public void onGlobalTurnEnded()
    {
        // Conditions + Ailments
        List<Condition> conditionsToRemove = new ArrayList<>();
        for (Condition c : conditions) if(c.decrementTurnsRemaining() == 0) conditionsToRemove.add(c);
        for(Condition c : conditionsToRemove) if(c instanceof Ailment)
        {
            AilmentRemoveEvent are = new AilmentRemoveEvent((Ailment)c, this);
            battle.registerEvent(are);
        } else conditions.remove(c);

        // StatModifiers
        List<StatModifier> statModifiersToRemove = new ArrayList<>();
        for (StatModifier sm : statModifiers) if(sm.decrementTurnsRemaining() == 0) statModifiersToRemove.add(sm);

        for(StatModifier sm : statModifiersToRemove)
        {
            StatModifierRemoveEvent smre = new StatModifierRemoveEvent(sm, this);
            battle.registerEvent(smre);
            if(!smre.isDisallowed()) smre.perform();
        }

        // Action Cooldowns
        for (Iterator<Action> it = actionCooldowns.keySet().iterator(); it.hasNext(); )
        {
            Action a = it.next();
            int cd = actionCooldowns.get(a);
            actionCooldowns.put(a, cd-1);
            if (actionCooldowns.get(a) == 0) { it.remove(); }
        }

        performEliminationCheck();
    }

    /**
     * Called when it is this Actor's turn. Since this method overridden by the {@code Player} class, this is the default
     * implementation of the computer Actor behavior.
     * @see Player
     */
    public void onActorTurn()
    {
        if(performEliminationCheck()) return; // TODO extract this check to the battle class itself

        if(!eligibleToAct)
        {
            battle.println(Colorizer.RESET + name + " cannot move.");
            if(!eligibleToAct) eligibleToAct = true;
        } else onUserTurn();

        performEliminationCheck();
    }

    /**
    * Called when it is the Actor's turn. Can be overridden to modify the decision behavior of the Actor.
    */
    public void onUserTurn()
    {
        List<Action> actions = getUsableActions();

        WeightedSelector<Action> ws = new WeightedSelector<>(actions);
        if(ws.isEmpty()) ws.put(getPassingAction(), .25d);
        Action action = ws.getRandom();

        List<Actor> potentialTargets = action.isSupportive() ? battle.getFriendlies(this, true) : battle.getOpposition(this, true);
        Actor target = potentialTargets.get(random.nextInt(potentialTargets.size()));
        doAction(action, target);
    }

    /**
     * Performs a check that registers an {@code ActorDeathEvent} if the Actor (1) hasn't yet been eliminated and (2) isn't alive.
     * @return A boolean indicating whether or not the user is eliminated (i.e. where {@code isAlive()} returns false).
     * @implNote Updates the state of the {@code eliminated} boolean.
     */
    private boolean performEliminationCheck()
    {
        if (!isAlive() && !eliminated)
        {
            eliminated = true;
            battle.registerEvent(new ActorDeathEvent(this));
        }
        return eliminated;
    }

    public String toString()
    {
        return Colorizer.RESET + getAndFormatThisCombatantIndex() + " " + name + Colorizer.RESET;
    }

    public String getAndFormatThisCombatantIndex()
    {

        return Colorizer.GRAY + Colorizer.ITALIC +
                "[" + getTeam() + TextFormatter.subscript(String.valueOf(battle.getCombatantIndex(this)))
                + Colorizer.GRAY + Colorizer.ITALIC + "]" + Colorizer.RESET;
    }

    public static Action getPassingAction()
    {
        return new Action(Action.ActionType.STATUS, "Otiose", "Do absolutely nothing!", false, false, false)
        {
            @Override public void commit(Actor user, Actor target) { user.battle.println(user + " used " + getName() + ", doing absolutely nothing!"); }
        };
    }

    public Team getTeam()
    {
        Team[] theTeams = battle.teams;
        for(Team t : theTeams)
            if(t.getActors().contains(this))
                return t;
        return null;
    }
}

