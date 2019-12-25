package me.ubuntoof.Characters;

import me.ubuntoof.Actions.*;
import me.ubuntoof.Utils.Colorizer;
import me.ubuntoof.Utils.TextFormatter;
import me.ubuntoof.Utils.UserInputReader;

public class Player extends Actor {

    int exp = 0;
    int expTillNextLevel;
    private Action using;
    private Actor targetActor;

    public Player(String name, Action[] actions, int level) {
        super(name, actions, level);

        setBaseMaxHealth(10 + level);
        setBaseHealth(getBaseMaxHealth());
        setBaseStrength((int)(5 + Math.sqrt(level) + level/3));
        setBaseSpeed((int)(5 + Math.sqrt(level) + level/3));
        expTillNextLevel = level;
    }

    public static Action[] getDefaultActions() { return new Action[]{new Bop()}; }

    @Override
    public void onUserTurn()
    {
        using.commit(this, targetActor);
    }

    @Override
    public void onGlobalTurnStarted()
    {
        Colorizer.clear();
        if(isAlive())
        {
            getBattle().displayGlobalBattle(this);
            promptUserTurnInput();
        }
    }

    @Override
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

    private void promptUserTurnInput()
    {
        using = null;
        boolean wereActionsShown = false;

        while(using == null)
        {
            // prompt user to select an action
            System.out.println(Colorizer.RESET + Colorizer.UNDERLINE + "Select an action" + Colorizer.RESET + ": ");
            if(!wereActionsShown) for(int i = 0; i < getActions().length; i++) System.out.println(Colorizer.LIGHT_GRAY + Colorizer.BOLD + "[" + i + "] " + Colorizer.RESET + TextFormatter.formatAction(getActions()[i]));
            wereActionsShown = true;
            String input = UserInputReader.getResponse().toLowerCase().trim();
            if(checkForCommandInput(input)) continue; // used so input error feedback doesn't run

            Action matchingAction = null;
            int matches = 0;
            for(Action action : getActions()) if(action.getName().toLowerCase().contains(input))
            {
                matches++;
                matchingAction = action;
            }

            if(matches == 1) { using = matchingAction; break; }
            else if(matches > 1) { System.out.println(TextFormatter.formatError("More than one action was found with that input.")); continue; }

            try
            {
                using = getActions()[Integer.parseInt(input)];
            } catch(Exception ignored) { System.out.println(TextFormatter.formatError("No actions were found with that input.")); }
            /*else System.out.println(TextFormatter.formatError("No actions were found with that input."));*/
        }

        while(using.getRequiresTarget())
        {
            System.out.println(Colorizer.RESET + Colorizer.UNDERLINE + "Select a target" + Colorizer.RESET + ": ");
            /*for(Actor actor : getBattle().getCombatants()) if(actor.isAlive()) System.out.println(" - " + actor);*/
            String input = UserInputReader.getResponse().toLowerCase().trim();
            if(checkForCommandInput(input)) continue;

            Actor matchingActor = null;
            int matches = 0;
            for(Actor actor : getBattle().getCombatants()) if(actor.isAlive() && actor.getName().toLowerCase().contains(input))
            {
                matches++;
                matchingActor = actor;
            }

            if(matches == 1) { targetActor = matchingActor; break; }
            else if(matches > 1) { System.out.println(TextFormatter.formatError("More than one potential target was found with that input.")); continue; }

            try
            {
                Actor a = getBattle().getCombatants().get(Integer.parseInt(input));
                if(a != null && a.isAlive()) { targetActor = a; break; }
            } catch(Exception ignored) { System.out.println(TextFormatter.formatError("No potential targets were found with that input.")); }
            /*else System.out.println(TextFormatter.formatError("No actions were found with that input."));*/
        }
    }

    public static boolean checkForCommandInput(String s)
    {
        String str = s.trim();
        String commandOperand = "/";
        if(!str.startsWith(commandOperand)) return false;

        String[] args = str.substring(commandOperand.length()).split(" ");

        if(args[0].equalsIgnoreCase("stats"))
        {
            System.out.println(TextFormatter.formatInfo("Stats command recognized."));
            Actor viewActor;
            int matches = 0;
            /*for(int i = 0; i < getBattle().getLivingCombatants().size(); i++)
            {

            }*/
        }

        else if(args[0].equalsIgnoreCase("desc"))
        {
            System.out.println(TextFormatter.formatInfo("Description command recognized."));
        }

        return true;
    }

}
