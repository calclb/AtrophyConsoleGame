package me.ubuntoof.passives;

import me.ubuntoof.characters.Player;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.actors.ActionCommitEvent;
import me.ubuntoof.events.actors.ActorDeathEvent;
import me.ubuntoof.handlers.Battle;
import me.ubuntoof.utils.Colorizer;

public class Persistence extends Passive
{
    private boolean userAttackPhase;
    public Persistence()
    {
        super();
        header = Colorizer.LIGHT_YELLOW + "Persistence〉" + Colorizer.RESET;
    }

    @Override public void activate()
    {
        Battle b = owner.getBattle();
        if(b.getOpposition(owner).length <= 1) return;
        String msg = (header + owner + " saw an opportunity to continue attacking.");
        b.println(msg);

        if(owner instanceof Player) ((Player) owner).onGlobalTurnStarted();
        owner.onActorTurn();
    }

    @Override public void notifyEvent(Event e) // called by the Actor class
    {
        if(!owner.isAlive() || !(e instanceof ActionCommitEvent || e instanceof ActorDeathEvent)) return;
        // trigger when user kills target
            // listen to ActionCommitEvent -- mutate state to true if user acts
            // listen until next ActionCommitEvent for an ActionDeathEvent
        Battle battle = owner.getBattle();
        if(e instanceof ActionCommitEvent)
        {
            ActionCommitEvent ace = (ActionCommitEvent) e;
            userAttackPhase = ace.user == owner;
            if(userAttackPhase) ace.target.onTurnChanged();
        }
        else // other instances are guaranteed to be of ActorDeathEvent
        {
            ActorDeathEvent ade = (ActorDeathEvent) e;
            if(!battle.getTeamOf(owner).getActors().contains(ade.actor) && userAttackPhase && battle.getOpposition(owner).length > 0) activate();
        }
    }
}
