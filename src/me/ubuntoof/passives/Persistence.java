package me.ubuntoof.passives;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.characters.Player;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.actors.ActionCommitEvent;
import me.ubuntoof.events.actors.ActorDeathEvent;
import me.ubuntoof.handlers.Battle;
import me.ubuntoof.utils.Colorizer;

public class Persistence extends Condition
{
    private boolean userAttackPhase;

    public Persistence()
    {
        super("Persistence", "Allows user to move again after defeating an enemy.");
    }

    private String getHeader() { return Colorizer.LIGHT_YELLOW + name + "〉" + Colorizer.RESET; }

    public void activate(Actor owner)
    {
        Battle b = owner.battle;
        String msg = (getHeader() + owner + " saw an opportunity to continue attacking.");
        b.println(msg);

        if(owner instanceof Player) ((Player) owner).onGlobalTurnStarted(); // TODO refactor to make more descriptive
        owner.onActorTurn();
    }

    /**
     * @param owner The owner of the Condition
     * @param e The event that the Condition could activate by
     */
    @Override
    public void on(Actor owner, Event e)
    {
        if(e instanceof ActionCommitEvent) userAttackPhase = owner == ((ActionCommitEvent)e).user;

        if(e instanceof ActorDeathEvent)
        {
            ActorDeathEvent ade = (ActorDeathEvent) e;
            Battle b = ade.actor.battle;
            if(!userAttackPhase || !owner.isAlive() || owner.getTeam().getActors().contains(ade.actor) || b.getOpposition(owner).size() > 0) return;
            if(b.getOpposition(owner).size() > 1) activate(owner);
        }

    }
}
