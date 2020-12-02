package me.ubuntoof.entities;

import me.ubuntoof.StatContainer;
import me.ubuntoof.actions.druid.druidimite.Bonk;

public class Druidimite extends Entity
{
    public Druidimite(int level)
    {
        super("Druidimite", level, new StatContainer(3, 1, 2));
        addActions(Bonk.class);
    }

    public Druidimite()
    {
        this(5);
    }
}
