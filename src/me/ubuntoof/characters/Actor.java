package me.ubuntoof.characters;

import me.ubuntoof.Stats;
import me.ubuntoof.actions.Action;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.actors.ActionCommitEvent;
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
import me.ubuntoof.listeners.BattleInteractions;
import me.ubuntoof.modifiers.Ailment;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.passives.Passive;
import me.ubuntoof.utils.Colorizer;

import java.util.*;

public abstract class Actor implements BattleInteractions
{
    private List<Action> actions = new ArrayList<>();
    private List<Passive> passives = new ArrayList<>();
    private Battle battle;
    private final List<StatModifier> statModifiers = new ArrayList<>();
    private final Set<Ailment> ailments = new HashSet<>();
    protected static final Random random = new Random();

    private int level;
    public final int expValue;
    private int exp = 0;
    private final String name;
    private boolean eligibleToAct = true;
    private boolean eliminated = false;

    private int baseMaxHealth;
    private int baseHealth;
    private int baseStrength;
    private int baseDefense;
    private int baseArmor;
    private int baseSpeed;

    public Actor(String name, int level)
    {
        this.name = name;
        this.level = level;
        this.expValue = level/2;
    }

    public Actor(String name, int level, int expValue)
    {
        this.name = name;
        this.level = level;
        this.expValue = expValue;
    }

    public Actor(String name, List<Action> actions, int level)
    {
        this.name = name;
        this.actions = actions;
        this.level = level;
        this.expValue = level/2;
    }

    public Actor(String name, List<Action> actions, int level, int expValue)
    {
        this.name = name;
        this.actions = actions;
        this.level = level;
        this.expValue = expValue;
    }

    public int getLevel() { return level; }
    public void levelUp() { level++; }
    public String getName() { return name; }

    public Battle getBattle() { return battle; }

    // Action-related methods
    public List<Action> getActions() { return actions; }
    public List<Passive> getPassives() { return passives; }

    // going to transition to the upper method
    public void doAction(Action action, Actor primaryTarget)
    {
        getBattle().battleInteractionsHandler.registerEvent(new ActionCommitEvent(this, action, primaryTarget));
        // action.commit(this, primaryTarget); (old)
    }

    public void doAction(int actionIndex, Actor primaryTarget)
    {
        getBattle().battleInteractionsHandler.registerEvent(new ActionCommitEvent(this, actions.get(actionIndex), primaryTarget));
        // actions.get(actionIndex).commit(this, primaryTarget); (old)
    }
    public boolean getEligibleToAct() { return eligibleToAct; }
    public void setEligibleToAct(boolean to) { eligibleToAct = to; }

    // Damage-related methods
    public int takeDamage(int dmg)
    {
        if(dmg < getArmor()) dmg = 0;
        else dmg = Math.max(dmg - getDefense(), 0);

        baseHealth -= dmg;
        return dmg;
    }

    public int takeDamage(int dmg, boolean trueDamage)
    {
        if(trueDamage)
        {
            baseHealth -= dmg;
        }
        else takeDamage(dmg);
        return dmg;
    }

    // Stat-related methods
    public int getBaseMaxHealth() { return baseMaxHealth; }
    public int getBaseStrength() { return baseStrength; }
    public int getBaseDefense() { return baseStrength; }
    public int getBaseArmor() { return baseStrength; }
    public int getBaseSpeed() { return baseSpeed; }

    public void setBaseMaxHealth(int maxHealth) { baseMaxHealth = maxHealth; }
    public void setBaseHealth(int health) { baseHealth = Math.min(health, baseMaxHealth); }
    public void setBaseStrength(int strength) { baseStrength = strength; }
    public void setBaseDefense(int defense) { baseDefense = defense; }
    public void setBaseArmor(int armor) { baseArmor = armor; }
    public void setBaseSpeed(int speed) { baseSpeed = speed; }

    public int getMaxHealth()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.MAX_HEALTH) mod *= sm.getMultiplier();
        return (int)(mod * baseMaxHealth);
    }

    public int getHealth()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.HEALTH) mod *= sm.getMultiplier();
        return (int)(mod * baseHealth);
    }

    public int getStrength()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.STRENGTH) mod *= sm.getMultiplier();
        return (int)(mod * baseStrength);
    }

    public int getDefense()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.DEFENSE) mod *= sm.getMultiplier();
        return (int)(mod * baseDefense);
    }

    public int getArmor()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.ARMOR) mod *= sm.getMultiplier();
        return (int)(mod * baseArmor);
    }

    public int getSpeed()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.SPEED) mod *= sm.getMultiplier();
        return (int)(mod * baseSpeed);
    }

    // State and modifier-related methods
    public boolean isAlive() { return baseHealth > 0d; }
    public Set<Ailment> getAilments() { return ailments; }
    public void addAilment(Ailment ailment)
    {
        for(Iterator<Ailment> it = ailments.iterator(); it.hasNext(); ) if(ailment.equals(it.next())) it.remove();
        // ailments.removeIf(a -> ailment.equals(a) && ailment.compareTo(a) > 0);
        getBattle().battleInteractionsHandler.registerEvent(new AilmentAddEvent(ailment, this)); // it.remove();
    }

    public List<StatModifier> getStatModifiers() { return statModifiers; }
    public void addStatModifier(StatModifier sm)
    {
        getBattle().battleInteractionsHandler.registerEvent(new StatModifierAddEvent(sm, this));
    }

    public String toString() { return getAndFormatThisCombatantIndex() + (this instanceof Player ? Colorizer.GRAY_BACKGROUND + " " + name + Colorizer.RESET : " " + name + Colorizer.RESET); }

    @Override public void notifyEvent(Event e)
    {
        if(e instanceof BattleStartEvent) onBattleStart(((BattleStartEvent) e).battle);
        if(e instanceof TurnChangeEvent) onTurnChanged();
        if(e instanceof GlobalTurnEndEvent) onGlobalTurnEnded();
        for(Passive passive : passives) passive.notifyEvent(e);
    }

    public void onBattleStart(Battle battle)
    {
        this.battle = battle;
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
            getBattle().println(Colorizer.RED + Colorizer.REVERSE + Colorizer.BOLD + "\uD83D\uDC80 " + name + " has been eliminated." + Colorizer.RESET);
            eliminated = true;
            getBattle().battleInteractionsHandler.registerEvent(new ActorDeathEvent(this));
        }
        return eliminated;
    }

    public String getAndFormatThisCombatantIndex()
    {
        if(this instanceof Player) return Colorizer.ITALIC + Colorizer.GRAY_BACKGROUND + "[" + getBattle().getCombatantIndex(this) + "]" + Colorizer.RESET;
        return Colorizer.ITALIC + Colorizer.GRAY + "[" + getBattle().getCombatantIndex(this) + "]" + Colorizer.RESET;
    }
}

