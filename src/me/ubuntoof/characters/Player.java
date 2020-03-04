package me.ubuntoof.characters;

import me.ubuntoof.actions.Action;
import me.ubuntoof.actions.Bop;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.state.BattleEndEvent;
import me.ubuntoof.events.state.GlobalTurnStartEvent;
import me.ubuntoof.utils.Colorizer;
import me.ubuntoof.utils.TextFormatter;
import me.ubuntoof.utils.UserInputReader;

import java.util.List;

public class Player extends Actor
{
    int exp = 0;
    int expTillNextLevel;
    private Action using;
    private Actor targetActor;

    public Player(String name, List<Action> actions, int level)
    {
        super(name, actions, level);

        setBaseMaxHealth(10 + level);
        setBaseHealth(getBaseMaxHealth());
        setBaseStrength((int)(5 + Math.sqrt(level) + level/3));
        setBaseSpeed((int)(5 + Math.sqrt(level) + level/3));
        expTillNextLevel = level;
    }

    public static Action[] getDefaultActions() { return new Action[]{new Bop()}; }

    @Override public void onUserTurn()
    {
        using.commit(this, targetActor);
    }

    @Override public void notifyEvent(Event e)
    {
        super.notifyEvent(e);
        if(e instanceof GlobalTurnStartEvent) onGlobalTurnStarted();
        if(e instanceof BattleEndEvent) onBattleEnded();
    }

    public void onGlobalTurnStarted()
    {
        Colorizer.clear();
        if(isAlive())
        {
            getBattle().displayGlobalBattle(this);
            promptUserTurnInput();
        }
    }

    public void onBattleEnded()
    {
        int gainedExp = 0;

        for(Actor actor : getBattle().getOpposition(this)) gainedExp += actor.expValue;

        addExp(gainedExp);

        System.out.println(Colorizer.WHITE + getName() + Colorizer.LIGHT_GRAY + " (Lv. " + getLevel() + ") " +
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
            System.out.println(Colorizer.LIGHT_YELLOW + Colorizer.BOLD + getName() + Colorizer.RESET + Colorizer.LIGHT_YELLOW + " leveled up to Lv. " + getLevel() + "!");
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
        else if(matches > 1) { System.out.println(TextFormatter.formatError("More than one potential target was found with that input.")); return null; }

        try
        {
            Actor a = getBattle().getCombatants().get(Integer.parseInt(s));
            if(a != null && a.isAlive()) return a;
        } catch(Exception ignored) { System.out.println(TextFormatter.formatError("No potential targets were found with that input.")); }

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
        else if(matches > 1) { System.out.println(TextFormatter.formatError("More than one action was found with that input.")); return null; }

        try
        {
            return getActions().get(Integer.parseInt(s));
        } catch(Exception ignored) { System.out.println(TextFormatter.formatError("No actions were found with that input.")); }

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
            System.out.println(Colorizer.RESET + Colorizer.UNDERLINE + "Select an action" + Colorizer.RESET + ": ");
            if(!wereActionsShown) for(int i = 0; i < getActions().size(); i++) System.out.println(Colorizer.LIGHT_GRAY + Colorizer.BOLD + "[" + i + "] " + Colorizer.RESET + TextFormatter.formatAction(getActions().get(i)));
            wereActionsShown = true;
            String input = UserInputReader.getResponse().toLowerCase().trim();
            if(!checkForCommandInput(input)) using = searchForActionWith(input);
        }

        while(using.getRequiresTarget() && targetActor == null)
        {
            System.out.println(Colorizer.RESET + Colorizer.UNDERLINE + "Select a target" + Colorizer.RESET + ": ");
            /*for(Actor actor : getBattle().getCombatants()) if(actor.isAlive()) System.out.println(" - " + actor);*/
            String input = UserInputReader.getResponse().toLowerCase().trim();
            if(!checkForCommandInput(input)) targetActor = searchForLivingActorWith(input);
        }
    }

    public boolean checkForCommandInput(String s)
    {
        String str = s.trim();
        String commandOperand = "/";
        if(!str.startsWith(commandOperand)) return false;

        String[] args = str.substring(commandOperand.length()).split(" ");

        if(args[0].equalsIgnoreCase("stats"))
        {
            System.out.println(TextFormatter.formatInfo("Stats command recognized."));

            Actor viewActor = null;
            while(viewActor == null)
            {
                try
                {
                    viewActor = searchForLivingActorWith(args[1]);
                } catch(ArrayIndexOutOfBoundsException ignored) { System.out.println(TextFormatter.formatError(Colorizer.BOLD + "Syntax: " + Colorizer.RESET + Colorizer.RED + " /stats <target>")); }
            }

        }

        else if(args[0].equalsIgnoreCase("desc"))
        {
            System.out.println(TextFormatter.formatInfo("Description command recognized."));
        }

        return true;
    }

}
