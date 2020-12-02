package me.ubuntoof;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.utils.Colorizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Team
{
    private final List<Actor> actors = new ArrayList<>();
    public final Actor[] originalActors;
    public final String icon;
    public final String format;

    public Team(Actor... as)
    {
        icon = Colorizer.Icons.getRandomValue();
        format = Colorizer.Colors.getRandomValue();
        actors.addAll(Arrays.asList(as));
        originalActors = as;
    }

    public Team(String icon, String format, Actor... as)
    {
        this.icon = icon;
        this.format = format;
        actors.addAll(Arrays.asList(as));
        originalActors = as;
    }

    public int size()
    {
        return actors.size();
    }

    /**
     * Returns a copy of the {@code actors} list in the object containing all actors on the team.
     */
    public List<Actor> getActors() { return new ArrayList<>(actors); }

    public void add(Actor a) { actors.add(a); }

    @Override public String toString()
    {
        return format + icon;
    }

    public boolean areAnyMembersAlive()
    {
        for(Actor a : actors) if(a.isAlive()) return true;
        return false;
    }
}
