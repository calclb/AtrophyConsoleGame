package me.ubuntoof;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Utils.Colorizer;

import java.util.ArrayList;
import java.util.Arrays;

public class Team
{
    public final String icon;
    public final String format;
    public final Actor[] originalActors;
    private ArrayList<Actor> actors = new ArrayList<>();

    public Team(Actor actor)
    {
        icon = Colorizer.Icons.getRandomValue();
        this.format = Colorizer.Colors.getRandomValue();
        originalActors = new Actor[]{actor};
    }

    public Team(Actor[] originalActors)
    {
        icon = Colorizer.Icons.getRandomValue();
        this.format = Colorizer.Colors.getRandomValue();
        this.originalActors = originalActors;
        actors.addAll(Arrays.asList(originalActors));
    }

    public ArrayList<Actor> getActors() { return actors; }

    public int size() { return actors.size(); }

    public String toString()
    {
        return format + icon;
    }

    public boolean areMembersAlive()
    {
        for(Actor a : actors) if(a.isAlive()) return true;
        return false;
    }
}
