package me.ubuntoof.characters;

import me.ubuntoof.StatContainer;
import me.ubuntoof.Stats;
import me.ubuntoof.actions.Action;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.actors.ActionCommitEvent;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.events.actors.ActorDeathEvent;
import me.ubuntoof.events.ailments.AilmentAddEvent;
import me.ubuntoof.events.ailments.AilmentRemoveEvent;
import me.ubuntoof.events.ailments.AilmentTriggerEvent;
import me.ubuntoof.events.state.BattleStartEvent;
import me.ubuntoof.events.state.GlobalTurnEndEvent;
import me.ubuntoof.events.state.TurnChangeEvent;
import me.ubuntoof.events.statmodifiers.StatModifierAddEvent;
import me.ubuntoof.events.statmodifiers.StatModifierRemoveEvent;
import me.ubuntoof.handlers.Battle;
import me.ubuntoof.handlers.BattleInteractions;
import me.ubuntoof.modifiers.Ailment;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.passives.Passive;
import me.ubuntoof.utils.Colorizer;

import java.util.*;

public abstract class Actor implements BattleInteractions
{
    // region - Permanent references & values
    private final List<StatModifier> statModifiers = new ArrayList<>();
    private final Set<Ailment> ailments = new HashSet<>();

    private final Map<Action, Integer> actionCooldowns = new HashMap<>();
    protected static final Random random = new Random();
    private final String name;
    // endregion

    // region - References & values that should not be changed during battle.
    protected final List<Action> signatureActions = new ArrayList<>();
    protected final List<Passive> signaturePassives = new ArrayList<>();
    private Battle battle;

    private int level;
    private int exp = 0;
    private final int expValue;
    // endregion.

    // region - Stats: Used for leveling and state management ONLY. Use StatModifiers to modify stats during battle.
    public final int baseStamina;
    public final int baseStrength;
    public final int baseIntelligence;
    public final int basePerception;
    public final int baseDexterity;
    public final int baseStealth;
    public final int baseAgility;

    protected int stamina;
    protected int strength;
    protected int intelligence;
    protected int dexterity;
    protected int perception;
    protected int stealth;
    protected int agility;
    // endregion

    // region - Battle-prone values
    private List<Action> actions = new ArrayList<>();
    private List<Passive> passives = new ArrayList<>();
    protected int health;
    private boolean eligibleToAct = true;
    private boolean eliminated = false;
    // endregion

    public Actor(String name, int level, StatContainer baseStats)
    {
        baseStamina = (int) baseStats.stamina;
        baseStrength = (int) baseStats.strength;
        baseIntelligence = (int) baseStats.intelligence;
        baseDexterity = (int) baseStats.dexterity;
        basePerception = (int) baseStats.perception;
        baseStealth = (int) baseStats.stealth;
        baseAgility = (int) baseStats.agility;
        copyBaseStats();

        this.name = name;
        this.level = level;
        this.expValue = level/2;
    }

    public Actor(String name, int level, int expValue, StatContainer baseStats)
    {
        baseStamina = (int) baseStats.stamina;
        baseStrength = (int) baseStats.strength;
        baseIntelligence = (int) baseStats.intelligence;
        baseDexterity = (int) baseStats.dexterity;
        basePerception = (int) baseStats.perception;
        baseStealth = (int) baseStats.stealth;
        baseAgility = (int) baseStats.agility;
        copyBaseStats();

        this.name = name;
        this.level = level;
        this.expValue = expValue;
    }

    public Actor(String name, List<Action> actions, int level, StatContainer baseStats)
    {
        baseStamina = (int) baseStats.stamina;
        baseStrength = (int) baseStats.strength;
        baseIntelligence = (int) baseStats.intelligence;
        baseDexterity = (int) baseStats.dexterity;
        basePerception = (int) baseStats.perception;
        baseStealth = (int) baseStats.stealth;
        baseAgility = (int) baseStats.agility;
        copyBaseStats();

        this.name = name;
        signatureActions.addAll(actions);
        this.level = level;
        this.expValue = level/2;
    }

    public Actor(String name, List<Action> actions, int level, int expValue, StatContainer baseStats)
    {
        baseStamina = (int) baseStats.stamina;
        baseStrength = (int) baseStats.strength;
        baseIntelligence = (int) baseStats.intelligence;
        baseDexterity = (int) baseStats.dexterity;
        basePerception = (int) baseStats.perception;
        baseStealth = (int) baseStats.stealth;
        baseAgility = (int) baseStats.agility;
        copyBaseStats();

        this.name = name;
        signatureActions.addAll(actions);
        this.level = level;
        this.expValue = expValue;
    }

    public void copyBaseStats()
    {
        stamina = baseStamina;
        health = baseStamina;
        strength = baseStrength;
        intelligence = baseIntelligence;
        dexterity = baseDexterity;
        perception = basePerception;
        stealth = baseStealth;
        agility = baseAgility;
    }

    public int getLevel() { return level; }
    public void levelUp()
    {
        level++;
    }
    public String getName() { return name; }
    public int getExpValue() { return expValue; }
    public Battle getBattle() { return battle; }

    // Action-related methods
    public List<Action> getSignatureActions() { return signatureActions; }
    public List<Passive> getSignaturePassives() { return signaturePassives; }

    public List<Action> getActions() { return actions; }
    public List<Passive> getPassives() { return passives; }


    public void addPassive(Passive p)
    {
        p.initOwner(this);
        passives.add(p);
    }

    public void removePassive(Passive passive)
    {
        for(int i = 0; i < passives.size(); i++)
        {
            Passive p = passives.get(i);
            if(p == passive)
            {
                passives.remove(p);
                i--;
            }
        }
    }

    // going to transition to the upper method
    public void doAction(Action action, Actor primaryTarget)
    {
        getBattle().battleInteractionsHandler.registerEvent(new ActionCommitEvent(this, action, primaryTarget));
        // action.commit(this, primaryTarget); (old)
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
     *
     * Should only be invoked by events. Use the {@code takeDamage()} method to make an {@code Actor} take damage.
     */
    public void takeRawDamage(int dmg, boolean trueDamage)
    {
        health = Math.max(health - dmg, 0);
        handleElimination();
    }

    // Damage-related methods
    public ActorDamageEvent takeDamage(int dmg)
    {
        //if(dmg < getArmor()) dmg = 0;
        //else dmg = Math.max(dmg - getDefense(), 0);

        ActorDamageEvent ade = new ActorDamageEvent(this, dmg, false);
        getBattle().battleInteractionsHandler.registerEvent(ade);
        return ade;
    }

    public ActorDamageEvent takeDamage(int dmg, boolean trueDamage)
    {
        ActorDamageEvent ade = new ActorDamageEvent(this, dmg, trueDamage);
        getBattle().battleInteractionsHandler.registerEvent(ade);
        return ade;
    }

    // Stat-related methods

    public int getStamina()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.STAMINA) mod *= sm.getMultiplier();
        return (int)(mod * stamina);
    }

    public int getHealth()
    {
        return health;
    }
    public void setHealth(int h) { health = h; }

    public int getStrength()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.STRENGTH) mod *= sm.getMultiplier();
        return (int)(mod * strength);
    }

    public int getIntelligence()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.INTELLIGENCE) mod *= sm.getMultiplier();
        return (int)(mod * intelligence);
    }

    public int getDexterity()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.DEXTERITY) mod *= sm.getMultiplier();
        return (int)(mod * dexterity);
    }

    public int getPerception()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.PERCEPTION) mod *= sm.getMultiplier();
        return (int)(mod * perception);
    }

    public int getStealth()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.STEALTH) mod *= sm.getMultiplier();
        return (int)(mod * stealth);
    }

    public int getAgility()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.AGILITY) mod *= sm.getMultiplier();
        return (int)(mod * agility);
    }

    // State and modifier-related methods
    public boolean isAlive() { return health > 0d; }

    /**
     * This method can be used to get or remove an {@code Ailment}.
     * Use {@code addAilment} to add an {@code Ailment}.
     */
    public Set<Ailment> getAilments() { return ailments; }
    public void addAilment(Ailment ailment)
    {
        //for(Iterator<Ailment> it = ailments.iterator(); it.hasNext(); ) if(ailment.equals(it.next())) it.remove();
        ailments.removeIf(ailment::equals);
        getBattle().battleInteractionsHandler.registerEvent(new AilmentAddEvent(ailment, this)); // it.remove();
    }

    /**
     * This method can be used to get or remove a {@code StatModifier}.
     * Use {@code addStatModifier} to add a {@code StatModifier}.
     */
    public List<StatModifier> getStatModifiers() { return statModifiers; }
    public void addStatModifier(StatModifier sm)
    {
        getBattle().battleInteractionsHandler.registerEvent(new StatModifierAddEvent(sm, this));
    }

    @Override public void onEvent(Event e)
    {
        if(e instanceof BattleStartEvent) onBattleStart(((BattleStartEvent) e).battle);
        if(e instanceof TurnChangeEvent) onTurnChanged();
        if(e instanceof GlobalTurnEndEvent) onGlobalTurnEnded();
        for(Passive p : passives) p.onEvent(e);
        passives.removeIf(Passive::getKilled);
    }

    public void onBattleStart(Battle battle)
    {
        this.battle = battle;
        assert actions != null && passives != null;
        actions = signatureActions;
        passives = signaturePassives;
        for(Passive passive : passives) passive.initOwner(this);
    }

    public void onTurnChanged()
    {
        handleElimination();
    }

    public void onGlobalTurnEnded()
    {
        List<StatModifier> removedModifiers = new ArrayList<>();

        for (Iterator<StatModifier> it = statModifiers.iterator(); it.hasNext(); )
        {
            StatModifier sm = it.next();
            assert sm.getDurationInTurns() >= 0;
            if (sm.getDurationInTurns() == 0) { it.remove(); removedModifiers.add(sm); }
        }

        for (Iterator<Action> it = actionCooldowns.keySet().iterator(); it.hasNext(); )
        {
            Action a = it.next();
            int cd = actionCooldowns.get(a);
            actionCooldowns.put(a, cd-1);
            if (actionCooldowns.get(a) == 0) { it.remove(); }
        }

        for(StatModifier sm : removedModifiers) getBattle().battleInteractionsHandler.registerEvent(new StatModifierRemoveEvent(sm, this));

        handleElimination();
    }

    public void onActorTurn()
    {
        List<Ailment> removedAilments = new ArrayList<>();

        if(!eliminated) for (Iterator<Ailment> it = ailments.iterator(); it.hasNext(); )
        {
            Ailment ailment = it.next();
            assert ailment.getDurationInTurns() >= 0;
            getBattle().battleInteractionsHandler.registerEvent(new AilmentTriggerEvent(ailment, this));
            if (ailment.getDurationInTurns() == 0) { it.remove(); removedAilments.add(ailment); }
        }

        for(Ailment ailment : removedAilments) getBattle().battleInteractionsHandler.registerEvent(new AilmentRemoveEvent(ailment, this));

        if(handleElimination()) return;

        if(!eligibleToAct)
        {
            getBattle().println(Colorizer.RESET + getName() + " cannot move.");
            if(!eligibleToAct) eligibleToAct = true;
        } else onUserTurn();
    }

    public abstract void onUserTurn();

    private boolean handleElimination()
    {
        if (!isAlive() && !eliminated)
        {
            eliminated = true;
            getBattle().battleInteractionsHandler.registerEvent(new ActorDeathEvent(this));
        }
        return eliminated;
    }

    public String toString() { return Colorizer.RESET + getAndFormatThisCombatantIndex() + (this instanceof Player ? Colorizer.GRAY_BACKGROUND : "") + " " + name + Colorizer.RESET; }

    public String getAndFormatThisCombatantIndex()
    {
        return (this instanceof Player ? Colorizer.GRAY_BACKGROUND : Colorizer.GRAY) + Colorizer.ITALIC + "[" + getBattle().getCombatantIndex(this) + "]" + Colorizer.RESET;
    }
}

