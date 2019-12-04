package me.ubuntoof.Characters;

import me.ubuntoof.Actions.*;
import me.ubuntoof.Utils.Colorizer;
import me.ubuntoof.Utils.UserInputReader;

public class Player extends Actor {

    int exp = 0;
    private Action using;
    private Actor targetActor;

    public Player(String name, Action[] actions, int level) {
        super(name, actions, level);

        setBaseMaxHealth(10 + level);
        setBaseHealth(getBaseMaxHealth());
        setBaseStrength((int)(4 + Math.sqrt(level)));
        setBaseSpeed((int)(4 + Math.sqrt(level)));
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
        promptUser();
    }

    @Override
    public void onBattleEnded()
    {

    }

    private void promptUser()
    {
        using = null;
        boolean validActionIndex = false;
        boolean erroneousInput = false;
        while(!validActionIndex)
        {
            if(!erroneousInput) System.out.println(Colorizer.UNDERLINE + "Select an action to use" + Colorizer.RESET + ": ");
            erroneousInput = false;

            for(int i = 0; i < getActions().length; i++)
            {
                System.out.println("[" + i + "] " + getActions()[i]);
            }

            String uinput = UserInputReader.getResponse().toLowerCase();


            int matches = 0;
            Action matchingAction = null;
            for(Action action : getActions())
            {

                if(action.getName().toLowerCase().contains(uinput))
                {
                    matches++;
                    matchingAction = action;
                }

            }

            if(matches == 1)
            {
                using = matchingAction;
                validActionIndex = true;
            }

            for(int i = 0; i < getActions().length; i++)
            {
                try
                {
                    using = getActions()[Integer.parseInt(uinput)];
                    validActionIndex = true;
                }
                catch (Exception e)
                {
                    erroneousInput = true;
                }
            }
        }

        boolean validTarget = false;
        while(!validTarget)
        {
            if(!erroneousInput) System.out.println(Colorizer.UNDERLINE + "Select an intended target" + Colorizer.RESET + ": ");
            erroneousInput = false;

            Actor[] combatants = getBattle().getCombatants();

            String uinput = UserInputReader.getResponse().toLowerCase();

            Actor matchingActor = null;
            int matches = 0;
            for(Actor actor : combatants)
            {

                if(actor.getName().toLowerCase().contains(uinput))
                {
                    matches++;
                    matchingActor = actor;
                }

            }

            if(matches == 1)
            {
                targetActor = matchingActor;
                validTarget = true;
            }

            for(int i = 0; i < getActions().length; i++)
            {
                try
                {
                    targetActor = combatants[Integer.parseInt(uinput)];
                    validTarget = true;
                }
                catch (Exception e)
                {
                    erroneousInput = true;
                }
            }

            if(erroneousInput) System.out.println("Please reference a target by typing a unique part of its name or the number associated to it.");
        }
    }
}
