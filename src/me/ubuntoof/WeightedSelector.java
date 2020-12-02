package me.ubuntoof;

import java.util.*;

public class WeightedSelector<T>
{
    private final Map<T, Double> weights = new HashMap<>();

    public WeightedSelector() {}
    public WeightedSelector(Collection<? extends T> c) { for(T obj : c) weights.put(obj, 1d); }
    public WeightedSelector(HashMap<T, Double> map)
    {
        putWeights(map);
    }

    public void putWeights(HashMap<T, Double> map)
    {
        map.replaceAll((k, v) -> v);
    }

    public WeightedSelector<T> put(T obj, double d)
    {
        weights.put(obj, d);
        return this;
    }

    public void clear() { weights.clear(); }

    public double sum()
    {
        double sum = 0;
        for(double d : weights.values()) sum += d;
        return sum;
    }

    public boolean isEmpty() { return weights.isEmpty(); }

    public double getProbabilityOfChoosing(T key) { return weights.get(key)/sum(); }

    public T getRandom()
    {
        Random r = new Random();
        int rIndex = -1; // should throw error if randomization fails
        double rand = r.nextDouble() * sum();

        // parallel list creation
        List<T> selections = new ArrayList<>(weights.keySet());
        List<Double> weightList = new ArrayList<>(weights.values());

        for(int i = 0; i < selections.size(); i++)
        {
            rand -= (double)weightList.toArray()[i];
            if(rand <= 0d) { rIndex = i; break; }
        }
        return selections.get(rIndex);
    }
}
