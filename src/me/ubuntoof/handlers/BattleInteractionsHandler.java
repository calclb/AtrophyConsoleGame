package me.ubuntoof.handlers;

import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;
import me.ubuntoof.listeners.BattleInteractions;

import java.util.ArrayList;
import java.util.List;

public class BattleInteractionsHandler
{
    private final List<BattleInteractions> interactions = new ArrayList<>();

    public void add(BattleInteractions l) { interactions.add(l); }
    public void addAll(List<BattleInteractions> l) { interactions.addAll(l); }
    public void remove(BattleInteractions l) { interactions.remove(l); }
    public void clear() { interactions.clear(); }

    public void registerEvent(Event e)
    {
        for(BattleInteractions bi : interactions) bi.notifyEvent(e);
        if(e instanceof Cancellable && !(((Cancellable) e).isCancelled())) e.perform();
    }
}
