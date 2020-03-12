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

    public void activate()
    {
        Battle b = owner.getBattle();
        //if(b.getOpposition(owner).size() <= 1) return;
        String msg = (header + owner + " saw an opportunity to continue attacking.");
        b.println(msg);

        if(owner instanceof Player) ((Player) owner).onGlobalTurnStarted();
        owner.onActorTurn();
    }

    public void onEvent(ActorDeathEvent e)
    {
        Battle battle = e.actor.getBattle();
        if(!userAttackPhase || !owner.isAlive() || battle.getTeamOf(owner).getActors().contains(e.actor) || battle.getOpposition(owner).size() > 0) return;
        activate();
    }

    public void onEvent(ActionCommitEvent e)
    {
        userAttackPhase = owner == e.user;
    }
}
