package me.ubuntoof.Passives;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Handlers.Battle;
import me.ubuntoof.Listeners.ActorInteractions;
import me.ubuntoof.Listeners.BattleInteractions;

import java.text.NumberFormat;
import java.util.Random;


// TODO work on this
public abstract class Passive implements ActorInteractions, BattleInteractions
{
    protected final static Random random = new Random();
    protected final static NumberFormat pf = NumberFormat.getPercentInstance();
    protected String header;
    protected Actor owner;

    public Passive(Actor owner)
    {
        this.owner = owner;
    }

    @Override
    public void onGlobalTurnStarted()
    {

    }

    @Override
    public void onBattleStarted(Battle battle)
    {

    }

    @Override
    public void onActorTurn() {

    }

    @Override
    public void onTurnChanged() {

    }

    public abstract void activate();
}
