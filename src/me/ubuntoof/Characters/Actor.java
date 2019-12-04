package me.ubuntoof.Characters;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Handlers.Battle;
import me.ubuntoof.Listeners.BattleInteractions;
import me.ubuntoof.Modifiers.Ailment;
import me.ubuntoof.Modifiers.StatModifier;
import me.ubuntoof.Stats;
import me.ubuntoof.Utils.Colorizer;

import java.util.*;

public abstract class Actor implements BattleInteractions {

    private Action[] actions;
    private Battle battle;
    private ArrayList<StatModifier> statModifiers = new ArrayList<>();
    private Set<Integer> disabledActionIndex = new HashSet<>();
    private Set<Ailment> ailments = new HashSet<>();
    protected final static Random random = new Random();

    private int level;
    private int exp;
    private String name;
    private boolean eligibleToAct = true;

    private int baseMaxHealth;
    private int baseHealth;
    private int baseStrength;
    private int baseDefense;
    private int baseArmor;
    private int baseSpeed;

    public Actor(String name, Action[] actions, int level)
    {
        this.name = name;
        this.actions = actions;
        this.level = level;
        exp = 0;
    }

    public int getLevel() { return level; }
    public int addExp(int expToAdd) { this.exp += expToAdd; return exp; }
    public String getName() { return name; }


    public Battle getBattle() { return battle; }

    // Action-related methods
    public Action[] getActions() { return actions; }
    protected void setAction(int index, Action action) { actions[index] = action; }

    // going to transition to the upper method
    public void doAction(Action action, Actor primaryTarget) { action.commit(this, primaryTarget); }

    public void doAction(int actionIndex, Actor primaryTarget) { actions[actionIndex].commit(this, primaryTarget); }
    public boolean getEligibleToAct() { return eligibleToAct; }
    public void setEligibleToAct(boolean to) { eligibleToAct = to; }

    // Damage-related methods
    public int takeDamage(int dmg)
    {
        if(dmg < getArmor()) dmg = 0;
        else dmg -= getDefense();

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

    public void onDamageTaken() {

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
        return baseHealth;
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
    public ArrayList<StatModifier> getStatModifiers() { return statModifiers; }

    public String toString() { return name; }

    @Override public void onBattleStarted(Battle battle) { this.battle = battle; }

    @Override public void onGlobalTurnEnded() {

        for (Iterator<StatModifier> it = statModifiers.iterator(); it.hasNext(); )
        {
            StatModifier sm = it.next();
            assert sm.getDurationInTurns() >= 0;
            if (sm.getDurationInTurns() == 0) statModifiers.remove(sm);
        }


        for (Iterator<Ailment> it = ailments.iterator(); it.hasNext(); )
        {
            Ailment ailment = it.next();
            assert ailment.getDurationInTurns() >= 0;
            ailment.applyEffects(this);
            if (ailment.getDurationInTurns() == 0) it.remove();
        }
    }
}
