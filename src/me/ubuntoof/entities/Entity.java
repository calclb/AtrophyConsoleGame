package me.ubuntoof.entities;

import me.ubuntoof.StatContainer;
import me.ubuntoof.actions.Action;
import me.ubuntoof.items.ConsumableItem;
import me.ubuntoof.passives.Condition;
import me.ubuntoof.utils.TextFormatter;
import me.ubuntoof.utils.UserInputReader;

import java.util.*;

public class Entity
{
    public static final int MAX_LEVEL = 100;

    // region - Instance data
    private final String name;
    private boolean playerControlled;
    private int level;
    private int exp = 0;

    private double baseStamina;
    private double baseStrength;
    private double baseAgility;

    public boolean isPlayerControlled() { return playerControlled; }
    public void setPlayerControlled(boolean b) { playerControlled = b; }

    /**
     * Class-based set of actions to be created when deriving an Actor object from the container.
     * The Actor class is given the responsibility to handle instantiation of the actions.
     * @see Action
     */
    private final Set<Class<? extends Action>> actions = new HashSet<>();

    /**
     * Class-based set of conditions to be created when deriving an Actor object from the container.
     * The Actor class is given the responsibility to handle instantiation of the conditions.
     * @see Condition
     */
    private final Set<Class<? extends Condition>> conditions = new HashSet<>();

    /**
     * List that contains consumable items (i)
     * @see ConsumableItem
     */
    private final List<ConsumableItem> consumables = new ArrayList<>();
    // endregion

    /**
     * Creates an EntityContainer, which is designed to store values of a given character so that they can be used across battles.
     * Ultimately, the purpose of this object is to serve as a basis for multiple {@code Actor}s to be derived, without reflecting mutations made in battle
     * in other objects (potentially causing unforeseen logical errors).
     *
     *
     * @param n name of the EntityContainer (copied into Actor objects for battles)
     * @param lv level of the entity; forcibly conforms to the expression {@code 1 <= lv <= 100}
     * @param baseStats StatContainer whose values are copied as integers into the EntityContainer
     * @see StatContainer
     */
    public Entity(String n, int lv, StatContainer baseStats)
    {
        name = n;
        level = Math.min(Math.max(1, lv), MAX_LEVEL);
        baseStamina = baseStats.stamina;
        baseStrength = baseStats.strength;
        baseAgility = baseStats.agility;
    }

    // region - Getters

    public String getName() { return name; }
    public int getLevel() { return level; }

    /**
     * @return experience required to level up.
     */
    public int getRequiredExpForNextLevel() { return level*2-exp;}

    /**
     * @return current experience gain.
     */
    public int getExp() { return exp; }

    public int getBaseStamina() { return (int)baseStamina; }
    public int getBaseStrength() { return (int)baseStrength; }
    public int getBaseAgility() { return (int)baseAgility; }

    /**
     * @return A copy of the actions as a List.
     * @see Action
     */
    public List<Class<? extends Action>> getActions() { return new ArrayList<>(actions); }

    /**
     * @return A copy of the passives as a List.
     * @see Condition
     */
    public List<Class<? extends Condition>> getConditions() { return new ArrayList<>(conditions); }
    // endregion

    /**
     * Adds {@code expToAdd} exp to the EntityContainer and modifies the level accordingly.
     * @return the number of levels grown.
     */
    public int addExp(int expToAdd)
    {
        exp += expToAdd;
        int previousLevel = level;
        int expTillNextLevel = getRequiredExpForNextLevel();

        while(exp >= expTillNextLevel)
        {
            exp -= expTillNextLevel;
            level++;
            growStats(new StatContainer(baseStamina, baseStrength, baseAgility));
            expTillNextLevel = getRequiredExpForNextLevel();
        }
        return level - previousLevel;
    }

    /**
     * @return A StatContainer that represents the character with {@code repetitions} levels grown.
     */
    public static StatContainer growStats(StatContainer sc /*, int repetitions /* levels grown */)
    {
        Random r = new Random();
        double sta = sc.stamina;
        double str = sc.strength;
        double agl = sc.agility;

        return new StatContainer(sta + Math.min(r.nextDouble()*2, .4), str + Math.min(r.nextDouble()*2, .4), agl + Math.min(r.nextDouble()*2, .4));
    }

    public static StatContainer growStats(StatContainer sc, int repetitions)
    {
        for(int i = 0; i < repetitions; i++) sc = growStats(sc);
        return sc;
    }

    /**
     * Adds a action via class to the PlayerContainer. A corresponding object should be created when an Actor is created from the container instance.
     * @see Action
     */
    @SafeVarargs public final void addActions(Class<? extends Action>... as) { actions.addAll(Arrays.asList(as)); }

    /**
     * Adds a passive via class to the PlayerContainer. A corresponding object should be created when an Actor is created from the container instance.
     * @see Condition
     */
    @SafeVarargs public final void addPassives(Class<? extends Condition>... ps) { conditions.addAll(Arrays.asList(ps)); }

    public List<ConsumableItem> getConsumables() { return new ArrayList<>(consumables); }
    public void setConsumables(ConsumableItem... cis)
    {
        consumables.clear();
        consumables.addAll(Arrays.asList(cis));
    }

    /**
     * Performs a dialogue with the user to gain necessary information to generate an EntityContainer. Either Players
     * or procedurally random Actors should use this method, since enemy archetypes have identical base stats and stat growth multipliers.
     * @return An EntityContainer with respect to the given parameters.
     */
    public static Entity generatePlayerContainerFromDialogue()
    {
        Random r = new Random();
        Scanner s = new Scanner(System.in);

        System.out.println("What is your name?");
        String name = UserInputReader.getResponseWhenRegexMatched( /*^*/"[a-zA-Z]+", TextFormatter.formatError("You may only use alphabetical characters."));

        int lv = 0;
        boolean isValidLevel;
        do
        {
            System.out.print("What level would you like to be (minimum of 1, maximum of 20)? ");
            try { lv = s.nextInt(); } catch(InputMismatchException e) { e.printStackTrace(); }
            isValidLevel = 1 <= lv && lv <= 20;
            if(!isValidLevel) System.out.println(TextFormatter.formatError("Invalid input Use a number from 1-20."));
        } while(!isValidLevel);

        StatContainer baseStats = new StatContainer(
                r.nextDouble()*5 + 5,
                r.nextDouble()*5 + 1,
                r.nextDouble()*5 + 1);

        Entity res = new Entity(name, lv, growStats(baseStats, lv));
        res.playerControlled = true;
        return res; // TODO rework so the Entity constructor immediately handles stat growth
    }

    @Override public String toString()
    {
        return name;
    }
}
