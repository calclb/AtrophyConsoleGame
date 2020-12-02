package me.ubuntoof.handlers;

import me.ubuntoof.Party;
import me.ubuntoof.WeightedSelector;
import me.ubuntoof.entities.*;
import me.ubuntoof.utils.Colorizer;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Tourney
{
    private final List<Party> competingParties = new ArrayList<>();
    private final Map<Integer, List<Party>> eliminatedParties = new HashMap<>();
    private final int rounds;
    private int roundNum;
    private final int teamsPerBattle = 2; // TODO to have winners/losers brackets; add later

    public Tourney(int actorsPerParty, int rounds, Party... presetTeams)
    {
        this.rounds = rounds;

        int teamsToGenerate = (int)(Math.pow(teamsPerBattle, rounds));

        for(int i = 0; i < presetTeams.length && i < teamsToGenerate; i++) competingParties.add(presetTeams[i]);
        for(int i = presetTeams.length; i < teamsToGenerate; i++) competingParties.add(generateParty(actorsPerParty));

    }

    public void runTourney()
    {
        while(++roundNum <= rounds)
        {
            StringBuilder sb = new StringBuilder(Colorizer.RESET + Colorizer.UNDERLINE + "Round " + roundNum + " Teams" + Colorizer.RESET + ":");
            for (Party p : competingParties) sb.append('\n').append(p);
            System.out.println(sb);

            Random r = new Random();
            List<Party> unusedTeams = new ArrayList<>(competingParties);
            List<Party> winners = new ArrayList<>();
            List<Party> losers = new ArrayList<>();

            Battle[] roundBattles = new Battle[unusedTeams.size() / teamsPerBattle];
            for(int i = 0; i < roundBattles.length; i++)
            {
                Party[] teamsToFight = new Party[teamsPerBattle];
                for(int j = 0; j < teamsToFight.length; j++) teamsToFight[j] = unusedTeams.remove(r.nextInt(unusedTeams.size()));

                roundBattles[i] = new Battle(teamsToFight);
                Party winner = roundBattles[i].run();

                for(Party t : teamsToFight) if(t == winner) winners.add(t);
            }

            for(Iterator<Party> it = competingParties.iterator(); it.hasNext(); )
            {
                Party t = it.next();
                if(winners.contains(t)) continue;
                losers.add(t);
                it.remove();
            }

            eliminatedParties.put(roundNum, losers);
        }

        System.out.println("WINNER: " + competingParties.get(0));
        //new StringBuilder(">>>").reverse().toString();
    }

    public void printTourneyResults()
    {

    }

    public Party generateParty(int size) // TODO grab random party name
    {
        return new Party("CPU Party", generateEntities(size));
    }

    public Entity[] generateEntities(int size)
    {
        Entity[] es = new Entity[size];

        WeightedSelector<Class<? extends Entity>> weightedSelector = new WeightedSelector<>();
        weightedSelector.put(Bandit.class, 1)
                .put(Druid.class, .65)
                .put(Goblin.class, .9)
                .put(Golem.class, .45)
                .put(Spaelcaster.class, .7);
        for(int i = 0; i < es.length; i++) try
        {
            Class<? extends Entity> clazz = weightedSelector.getRandom();
            es[i] = clazz.getConstructor(int.class).newInstance(5);
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) { e.printStackTrace(); }
        return es;
    }
}
