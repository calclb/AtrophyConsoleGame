package me.ubuntoof.characters;

import me.ubuntoof.actions.Action;
import me.ubuntoof.actions.Bop;
import me.ubuntoof.entities.Entity;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.state.GlobalTurnStartEvent;
import me.ubuntoof.handlers.Battle;
import me.ubuntoof.utils.Colorizer;
import me.ubuntoof.utils.TextFormatter;
import me.ubuntoof.utils.UserInputReader;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor
{
    int expTillNextLevel;
    private Action using;
    private Actor targetActor;

    public Player(Battle b, Entity e)
    {
        super(b, e);
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
    }

    public void onGlobalTurnStarted()
    {
        //print(Colorizer.clear());
        if(isAlive())
        {
            battle.displayScenario(this);
            promptUserTurnInput();
        }
    }

    public void onBattleEnd()
    {
        super.onBattleEnd();
        int gainedExp = getExp();
        entity.addExp(gainedExp);

        println(Colorizer.WHITE + name + Colorizer.LIGHT_GRAY + " (Lv. " + level + ") " +
                Colorizer.GRAY + Colorizer.REVERSE + "[" + Colorizer.RESET +
                TextFormatter.formatAsProgressBar("", gainedExp, expTillNextLevel, 20, Colorizer.LIGHT_BLUE + "▰", Colorizer.GRAY + "▱") +
                Colorizer.GRAY + Colorizer.REVERSE + "]" + Colorizer.RESET + Colorizer.BOLD + Colorizer.CYAN + " +" + gainedExp + " XP\n" + Colorizer.RESET);
    }

    public Actor searchForLivingActorWith(String s)
    {
        Actor matchingActor = null;
        int matches = 0;
        for(Actor actor : battle.getCombatants()) if(actor.isAlive() && actor.name.toLowerCase().contains(s))
        {
            matches++;
            matchingActor = actor;
        }

        if(matches == 1) { return matchingActor; }
        else if(matches > 1) { println(TextFormatter.formatError("More than one potential target was found with that input.")); return null; }

        try
        {
            Actor a = battle.getCombatants().get(Integer.parseInt(s));
            if(a != null && a.isAlive()) return a;
        } catch(Exception ignored) { println(TextFormatter.formatError("No target was found with that input.")); }

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
            /*for(Actor actor : getbattle.getCombatants()) if(actor.isAlive()) println(" - " + actor);*/
            String input = UserInputReader.getResponse().toLowerCase().trim();
            if(!checkForCommandInput(input)) targetActor = searchForLivingActorWith(input);
        }
        Colorizer.clear(battle.silent);
    }

    public boolean checkForCommandInput(String s)
    {
        String str = s.trim();
        String commandOperand = "/";
        if(!str.startsWith(commandOperand)) return false;

        String[] args = str.substring(commandOperand.length()).split(" ");

        switch(args[0]) {

            case "stats":
                TextFormatter.displayOnSecondCounter(TextFormatter.formatInfo("Stats command recognized"), 3);

                Actor viewActor = null;
                //while(viewActor == null)
                {
                    try
                    {
                        viewActor = searchForLivingActorWith(args[1]);
                        // TODO implement combatant stat viewing
                    } catch(Exception ignored) { TextFormatter.displayOnSecondCounter(TextFormatter.formatError(Colorizer.BOLD + "Syntax: " + Colorizer.RESET + Colorizer.RED + " /stats <target>"), 3); }
                }
                break;
            case "desc":
                TextFormatter.displayOnSecondCounter(TextFormatter.formatInfo("Description command recognized."), 3);
                break;
        }
        return true;
    }

    public String toString()
    {
        return Colorizer.RESET + getAndFormatThisCombatantIndex() + Colorizer.GRAY_BACKGROUND + " " + name + Colorizer.RESET;
    }

    public String getAndFormatThisCombatantIndex()
    {
        String actorFormat = Colorizer.RESET + Colorizer.GRAY_BACKGROUND;

        return actorFormat +
                Colorizer.ITALIC + "[" + (battle != null ? getTeam() + TextFormatter.subscript(String.valueOf(battle.getCombatantIndex(this))) : "")
                + actorFormat + Colorizer.ITALIC + "]" + Colorizer.RESET;
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
