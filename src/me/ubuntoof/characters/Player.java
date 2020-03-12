package me.ubuntoof.characters;

import me.ubuntoof.StatContainer;
import me.ubuntoof.actions.Action;
import me.ubuntoof.actions.Bop;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.state.BattleEndEvent;
import me.ubuntoof.events.state.GlobalTurnStartEvent;
import me.ubuntoof.passives.Passive;
import me.ubuntoof.utils.Colorizer;
import me.ubuntoof.utils.TextFormatter;
import me.ubuntoof.utils.UserInputReader;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor
{
    int exp = 0;
    int expTillNextLevel;
    private Action using;
    private Actor targetActor;

    public Player(String name, List<Action> actions, int level)
    {
        super(name, actions, level, new StatContainer(level + 5, 7, 7, 7, 7, 7, 7)); // TODO balance base stats
        expTillNextLevel = level;
    }

    public Player(String name, List<Action> actions, List<Passive> passives, int level)
    {
        super(name, actions, level, new StatContainer(level + 5, 7, 7, 7, 7, 7, 7));
        getSignaturePassives().addAll(passives);
        expTillNextLevel = level;
    }

    public static List<Action> getDefaultActions()
    {
        List<Action> defaults = new ArrayList<>();
        defaults.add(new Bop());
        return defaults;
    }

    @Override public void onUserTurn()
    {
        doAction(using, targetActor);
    }

    @Override public void onEvent(Event e)
    {
        super.onEvent(e);
        if(e instanceof GlobalTurnStartEvent) onGlobalTurnStarted();
        if(e instanceof BattleEndEvent) onBattleEnded();
    }

    public void onGlobalTurnStarted()
    {
        //print(Colorizer.clear());
        if(isAlive())
        {
            getBattle().displayGlobalBattle(this);
            promptUserTurnInput();
        }
    }

    public void onBattleEnded()
    {
        int gainedExp = 0;

        for(Actor actor : getBattle().getOpposition(this)) gainedExp += actor.getExpValue();

        addExp(gainedExp);

        println(Colorizer.WHITE + getName() + Colorizer.LIGHT_GRAY + " (Lv. " + getLevel() + ") " +
                Colorizer.GRAY + Colorizer.REVERSE + "[" + Colorizer.RESET +
                TextFormatter.formatAsProgressBar("", exp, expTillNextLevel, 20, Colorizer.LIGHT_BLUE + "▰", Colorizer.GRAY + "▱") +
                Colorizer.GRAY + Colorizer.REVERSE + "]" + Colorizer.RESET + Colorizer.BOLD + Colorizer.CYAN + " +" + gainedExp + " XP\n" + Colorizer.RESET);
    }


    public int addExp(int expToAdd)
    {
        exp += expToAdd;
        while(exp >= expTillNextLevel)
        {
            exp -= expTillNextLevel;
            levelUp();
            expTillNextLevel = getLevel();
            println(Colorizer.LIGHT_YELLOW + Colorizer.BOLD + getName() + Colorizer.RESET + Colorizer.LIGHT_YELLOW + " leveled up to Lv. " + getLevel() + "!");
        }
        return exp;
    }

    public Actor searchForLivingActorWith(String s)
    {
        Actor matchingActor = null;
        int matches = 0;
        for(Actor actor : getBattle().getCombatants()) if(actor.isAlive() && actor.getName().toLowerCase().contains(s))
        {
            matches++;
            matchingActor = actor;
        }

        if(matches == 1) { return matchingActor; }
        else if(matches > 1) { println(TextFormatter.formatError("More than one potential target was found with that input.")); return null; }

        try
        {
            Actor a = getBattle().getCombatants().get(Integer.parseInt(s));
            if(a != null && a.isAlive()) return a;
        } catch(Exception ignored) { println(TextFormatter.formatError("No potential targets were found with that input.")); }

        return null;
    }

    public Action searchForActionWith(String s)
    {
        Action matchingAction = null;
        int matches = 0;
        for(Action action : getActions()) if(action.getName().toLowerCase().contains(s))
        {
            matches++;
            matchingAction = action;
        }

        if(matches == 1) { return matchingAction; }
        else if(matches > 1) { println(TextFormatter.formatError("More than one action was found with that input.")); return null; }

        try
        {
            return getActions().get(Integer.parseInt(s));
        } catch(Exception ignored) { println(TextFormatter.formatError("No actions were found with that input.")); }

        return null;
    }

    private void promptUserTurnInput()
    {
        using = null;
        targetActor = null;
        boolean wereActionsShown = false;

        while(using == null)
        {
            // prompt user to select an action
            println(Colorizer.RESET + Colorizer.UNDERLINE + "Select an action" + Colorizer.RESET + ": ");
            if(!wereActionsShown) for(int i = 0; i < getActions().size(); i++) println(Colorizer.LIGHT_GRAY + Colorizer.BOLD + "[" + i + "] " + Colorizer.RESET + TextFormatter.formatAction(getActions().get(i)));
            wereActionsShown = true;
            String input = UserInputReader.getResponse().toLowerCase().trim();
            if(!checkForCommandInput(input)) using = searchForActionWith(input);
        }

        while(using.getRequiresTarget() && targetActor == null)
        {
            println(Colorizer.RESET + Colorizer.UNDERLINE + "Select a target" + Colorizer.RESET + ": ");
            /*for(Actor actor : getBattle().getCombatants()) if(actor.isAlive()) println(" - " + actor);*/
            String input = UserInputReader.getResponse().toLowerCase().trim();
            if(!checkForCommandInput(input)) targetActor = searchForLivingActorWith(input);
        }
        Colorizer.clear(getBattle().silent);
    }

    public boolean checkForCommandInput(String s)
    {
        String str = s.trim();
        String commandOperand = "/";
        if(!str.startsWith(commandOperand)) return false;

        String[] args = str.substring(commandOperand.length()).split(" ");

        if(args[0].equalsIgnoreCase("stats"))
        {
            TextFormatter.displayOnSecondCounter(TextFormatter.formatInfo("Stats command recognized"), 3);

            Actor viewActor = null;
            //while(viewActor == null)
            {
                try
                {
                    viewActor = searchForLivingActorWith(args[1]);
                } catch(Exception ignored) { TextFormatter.displayOnSecondCounter(TextFormatter.formatError(Colorizer.BOLD + "Syntax: " + Colorizer.RESET + Colorizer.RED + " /stats <target>"), 3); }
            }

        }

        else if(args[0].equalsIgnoreCase("desc"))
        {
            TextFormatter.displayOnSecondCounter(TextFormatter.formatInfo("Description command recognized."), 3);
        }

        return true;
    }
    
    private static void print(String s)
    {
        Colorizer.print(s);
    }
    private static void println(String s)
    {
        Colorizer.println(s);
    }
    private static void println()
    {
        Colorizer.println();
    }

}
