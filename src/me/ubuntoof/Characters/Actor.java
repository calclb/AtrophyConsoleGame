package me.ubuntoof.Characters;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Handlers.Battle;
import me.ubuntoof.Listeners.TurnListener;
import me.ubuntoof.Modifiers.Ailment;
import me.ubuntoof.Modifiers.StatModifier;
import me.ubuntoof.Stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Actor implements TurnListener {

    private Action[] actions;
    private Battle battle;
    private ArrayList<StatModifier> statModifiers = new ArrayList<>();
    private Set<Integer> disabledActionIndex = Collections.emptySet();
    private Set<Ailment> ailments = Collections.emptySet();

    private int level;
    private String name;
    private boolean eligibleToAct = true;

    private double baseMaxHealth;
    private double baseHealth;
    private double baseStrength;
    private double baseDefense;
    private double baseArmor;
    private double baseSpeed;

    public Actor(Battle battle, String name, Action[] actions, int level)
    {
        this.battle = battle;
        this.name = name;
        this.actions = actions;
        this.level = level;
    }

    public int getLevel() { return level; }

    protected Battle getBattle() { return battle; }

    // Action-related methods
    public Action[] getActions() { return actions; }
    protected void setAction(int index, Action action) { actions[index] = action; }
    public void doAction(Actor target, int actionIndex) { actions[actionIndex].commit(this, target); }
    public boolean getEligibleToAct() { return eligibleToAct; }
    public void setEligibleToAct(boolean to) { eligibleToAct = to; }

    // Damage-related methods
    public void takeDamage(double dmg) { baseHealth -= dmg; /* apply damage reduction here*/ }
    public void takeDamage(double dmg, boolean trueDamage) { baseHealth -= dmg; }

    // Probably going to be replaced at some point.
    public double getWeight()
    {
        double w = 0;
        w += baseMaxHealth;
        w += baseHealth;
        w += baseStrength*2;
        w += baseDefense*2;
        w += baseArmor*2;
        w += baseSpeed;
        w += actions.length*4;
        w += level;
        return w;
    }

    // Stat-related methods
    public double getBaseMaxHealth() { return baseMaxHealth; }
    public double getBaseStrength() { return baseStrength; }
    public double getBaseDefense() { return baseStrength; }
    public double getBaseArmor() { return baseStrength; }
    public double getBaseSpeed() { return baseSpeed; }

    public void setBaseMaxHealth(double maxHealth) { baseMaxHealth = maxHealth; }
    public void setBaseHealth(double health) { baseHealth = Math.min(health, baseMaxHealth); }
    public void setBaseStrength(double strength) { baseStrength = strength; }
    public void setBaseDefense(double defense) { baseDefense = defense; }
    public void setBaseArmor(double armor) { baseArmor = armor; }
    public void setBaseSpeed(double speed) { baseSpeed = speed; }

    public double getMaxHealth()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.MAX_HEALTH) mod *= sm.getMultiplier();
        return mod * baseMaxHealth;
    }

    public double getHealth()
    {
        return baseHealth;
    }

    public double getStrength()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.STRENGTH) mod *= sm.getMultiplier();
        return mod * baseStrength;
    }

    public double getDefense()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.DEFENSE) mod *= sm.getMultiplier();
        return mod * baseDefense;
    }

    public double getArmor()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.ARMOR) mod *= sm.getMultiplier();
        return mod * baseArmor;
    }

    public double getSpeed()
    {
        double mod = 1d;
        for(StatModifier sm : statModifiers) if(sm.getModifierType() == Stats.SPEED) mod *= sm.getMultiplier();
        return mod * baseSpeed;
    }

    // State and modifier-related methods
    public boolean isAlive() { return baseHealth > 0d; }
    public Set<Ailment> getAilments() { return ailments; }
    public ArrayList<StatModifier> getStatModifiers() { return statModifiers; }
    public void applyAilmentEffects() { for(Ailment ailment : ailments) ailment.applyEffects(this); }
    public String toString() { return name; }

}
