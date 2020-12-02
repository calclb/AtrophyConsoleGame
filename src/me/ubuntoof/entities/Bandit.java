package me.ubuntoof.entities;

import me.ubuntoof.StatContainer;
import me.ubuntoof.actions.bandit.ChipAway;
import me.ubuntoof.actions.bandit.Lunge;

public class Bandit extends Entity
{
    public Bandit(int lv)
    {
        super("Bandit", lv, new StatContainer(5, 2, 4));
        addActions(ChipAway.class, Lunge.class);
    }
}
