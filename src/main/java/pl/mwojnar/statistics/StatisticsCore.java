package pl.mwojnar.statistics;

import pl.mwojnar.core.Configuration;
import pl.mwojnar.elements.IMapElement;
import pl.mwojnar.elements.IMovableElement;
import pl.mwojnar.map.IMapChangeObserver;
import pl.mwojnar.utils.MapDirection;
import pl.mwojnar.utils.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class StatisticsCore implements IMapChangeObserver {

    private final List<Long> genesCounter;
    private int numberOfAnimals;
    private double energyOfAnimals;
    private int numberOfDeadAnimals;
    private double lifeTimeOfDeadAnimals;
    private int numberOfPlants;
    private double numberOfChildren;

    public StatisticsCore() {
        this.numberOfAnimals = 0;
        this.energyOfAnimals = 0;
        this.numberOfDeadAnimals = 0;
        this.lifeTimeOfDeadAnimals = 0;
        this.numberOfPlants = 0;
        // This value is assigned negative at first, because we don't want to count
        // animals that are placed at configuration as children
        this.numberOfChildren = -2 * Configuration.getStartNumberOfAnimals();

        this.genesCounter = new ArrayList<>();
        for (int i = 0; i < MapDirection.NUMBER_OF_DIRECTIONS; i++)
            genesCounter.add(0L);
    }

    @Override
    public void elementAdded(IMapElement element) {
        if (element instanceof IMovableElement movable) {
            numberOfAnimals++;
            energyOfAnimals += movable.getLifeEnergy();
            // We add two children, because every animals has two parents
            numberOfChildren += 2;
        }
        else if (element.isEdible()) {
            numberOfPlants++;
        }
    }

    @Override
    public void elementRemoved(IMapElement element) {
        if (element instanceof IMovableElement movable) {
            numberOfDeadAnimals++;
            numberOfAnimals--;
            lifeTimeOfDeadAnimals += movable.getLifeTime();
            energyOfAnimals -= Math.max(0, movable.getLifeEnergy());
            numberOfChildren -= movable.getChildrenNumber();
        }
        else if (element.isEdible()) {
            // We just eat something, so energy of animals is raising
            energyOfAnimals += Configuration.getPlantEnergy();
            numberOfPlants--;
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        energyOfAnimals -= Configuration.getMoveEnergy();
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public int getNumberOfPlants() {
        return numberOfPlants;
    }

    public double getMeanLifeEnergy() {
        return energyOfAnimals / numberOfAnimals;
    }

    public double getMeanLifeTime() {
        return lifeTimeOfDeadAnimals / numberOfDeadAnimals;
    }

    public double getMeanNumberOfChildren() {
        return numberOfChildren / numberOfAnimals;
    }

    public List<Long> getGenesCounter() {
        return genesCounter;
    }

    public void updateGenesCounter(List<IMovableElement> movables) {
        for (var movable : movables) {
            for (var gene : movable.getGenes())
                genesCounter.set(gene, genesCounter.get(gene) + 1);
        }
    }

    public int mainGeneIndex() {
        return mainGeneIndex(genesCounter);
    }

    public int mainGeneIndex(List<Long> genesCounter) {
        long maxValue = 0;
        int maxIndex = 0;

        for (int i = 0; i < genesCounter.size(); i++) {
            if (genesCounter.get(i) > maxValue) {
                maxValue = genesCounter.get(i);
                maxIndex = i;
            }
        }

        return maxIndex;
    }
}
