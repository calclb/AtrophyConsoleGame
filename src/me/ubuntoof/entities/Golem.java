package me.ubuntoof.entities;

import me.ubuntoof.StatContainer;
import me.ubuntoof.actions.golem.Obliderate;

public class Golem extends Entity
{

    public Golem(int level)
    {
        super("Golem", level, new StatContainer(25, 20, 1)); // TODO balance
        addActions(Obliderate.class);
    }

    public Golem()
    {
        this(5);
    }
}
