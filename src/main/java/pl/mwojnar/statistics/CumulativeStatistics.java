package pl.mwojnar.statistics;

import pl.mwojnar.controllers.INextCycleObserver;
import pl.mwojnar.controllers.NextCycleTask;
import pl.mwojnar.utils.MapDirection;

import java.util.ArrayList;
import java.util.List;

public class CumulativeStatistics implements INextCycleObserver {

    private final StatisticsCore core;
    private final int endEpoch;
    private final int n;

    private final List<Long> genesCounter;
    private double numberOfAnimals;
    private double lifeEnergy;
    private double lifeTime;
    private double numberOfPlants;
    private double numberOfChildren;

    public CumulativeStatistics(StatisticsCore core, NextCycleTask task, int endEpoch) {
        this.core = core;
        task.addObserver(this);

        this.endEpoch = endEpoch;
        this.n = endEpoch - task.getEpoch();
        this.numberOfAnimals = 0;
        this.lifeEnergy = 0;
        this.lifeTime = 0;
        this.numberOfPlants = 0;
        this.numberOfChildren = 0;

        this.genesCounter = new ArrayList<>();
        for (int i = 0; i < MapDirection.NUMBER_OF_DIRECTIONS; i++)
            genesCounter.add(0L);
    }

    @Override
    public void cycleFinished() {
        numberOfAnimals += core.getNumberOfAnimals();
        lifeEnergy += core.getMeanLifeEnergy();
        lifeTime += core.getMeanLifeTime();
        numberOfPlants += core.getNumberOfPlants();
        numberOfChildren += core.getMeanNumberOfChildren();

        for (int i = 0; i < genesCounter.size(); i++)
            genesCounter.set(i, genesCounter.get(i) + core.getGenesCounter().get(i));
    }

    @Override
    public void stoppedAfterNEpochs() { }

    public List<Long> getGenesCounter() {
        var result = new ArrayList<Long>(genesCounter.size());
        for (Long genes : genesCounter)
            result.add(genes / n);

        return result;
    }

    public double getNumberOfAnimals() {
        return numberOfAnimals / n;
    }

    public double getLifeEnergy() {
        return lifeEnergy / n;
    }

    public double getLifeTime() {
        return lifeTime / n;
    }

    public double getNumberOfPlants() {
        return numberOfPlants / n;
    }

    public double getNumberOfChildren() {
        return numberOfChildren / n;
    }

    public int getEndEpoch() {
        return endEpoch;
    }

    public int getN() {
        return n;
    }

    @Override
    public String toString() {
        var genes = getGenesCounter();
        long genesNumber = genes.stream().mapToLong(i -> i).sum();
        return "Number of animals:  " + roundToTwoPlaces(getNumberOfAnimals()) +
                "\nNumber of plants:  " + roundToTwoPlaces(getNumberOfPlants()) +
                "\nLife energy:  " + roundToTwoPlaces(getLifeEnergy()) +
                "\nLife time:  " + roundToTwoPlaces(getLifeTime()) +
                "\nNumber of children:  " + roundToTwoPlaces(getNumberOfChildren()) +
                "\n\nMain genes [%]:" +
                "\n0:  " + Math.round(100 * genes.get(0) / genesNumber) +
                "\n1:  " + Math.round(100 * genes.get(1) / genesNumber) +
                "\n2:  " + Math.round(100 * genes.get(2) / genesNumber) +
                "\n3:  " + Math.round(100 * genes.get(3) / genesNumber) +
                "\n4:  " + Math.round(100 * genes.get(4) / genesNumber) +
                "\n5:  " + Math.round(100 * genes.get(5) / genesNumber) +
                "\n6:  " + Math.round(100 * genes.get(6) / genesNumber) +
                "\n7:  " + Math.round(100 * genes.get(7) / genesNumber);
    }

    private double roundToTwoPlaces(double d) {
        return (double) Math.round(100 * d) / 100;
    }
}
