package pl.mwojnar.elements;

import pl.mwojnar.map.IWorldMap;
import pl.mwojnar.utils.MapDirection;
import pl.mwojnar.utils.Vector2d;

import java.util.*;

public class Animal implements IMovableElement {

    public static final int NEW_ANIMAL_ENERGY_RATIO = 4;
    public static final int GENES_LENGTH = 32;

    private final List<IElementChangeObserver> observers;
    private final IWorldMap map;
    private final Random random;

    private final List<Integer> genes;
    private Vector2d position;
    private MapDirection direction;
    private int lifeEnergy;
    private int lifeTime;
    private int numberOfChildren;

    public Animal(IWorldMap map, Vector2d position, int lifeEnergy, List<Integer> genes) {
        if (genes.size() != GENES_LENGTH)
            throw new IllegalArgumentException("Incorrect animal genes size");

        if (genes.stream().anyMatch(g -> g < 0 || g >= MapDirection.NUMBER_OF_DIRECTIONS))
            throw new IllegalArgumentException("Incorrect animal gene");

        this.observers = new LinkedList<>();
        this.random = new Random();

        this.direction = MapDirection.NORTH.rotate(random.nextInt(MapDirection.NUMBER_OF_DIRECTIONS));
        this.numberOfChildren = 0;
        this.lifeEnergy = lifeEnergy;
        this.lifeTime = 0;
        this.position = position;
        this.genes = genes;
        this.map = map;

        map.place(this);
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public boolean isEdible() {
        return false;
    }

    @Override
    public int getLifeEnergy() {
        return lifeEnergy;
    }

    @Override
    public int getLifeTime() {
        return lifeTime;
    }

    @Override
    public int getChildrenNumber() {
        return numberOfChildren;
    }

    @Override
    public void addEnergy(int energy) {
        lifeEnergy += energy;
    }

    @Override
    public int takeEnergyForNewElement() {
        var lostEnergy = lifeEnergy / NEW_ANIMAL_ENERGY_RATIO;
        lifeEnergy -= lostEnergy;
        numberOfChildren++;
        return lostEnergy;
    }

    @Override
    public List<Integer> getGenes() {
        return genes;
    }

    @Override
    public List<Long> getGenesCounter() {
        var genesCounter = new ArrayList<Long>(MapDirection.NUMBER_OF_DIRECTIONS);
        for (int i = 0; i < MapDirection.NUMBER_OF_DIRECTIONS; i++)
            genesCounter.add(0L);

        for (var gene : genes)
            genesCounter.set(gene, genesCounter.get(gene) + 1);

        return genesCounter;
    }

    @Override
    public void checkState() {
        if (lifeEnergy <= 0) {
            var observersCopy = new LinkedList<>(observers);
            observersCopy.forEach(o -> o.elementRemoved(this));
        }
    }

    @Override
    public void move(int moveEnergy) {
        var oldPosition = position;

        position = map.fitInside(position.add(direction.toUnitVector()));
        direction = direction.rotate(genes.get(random.nextInt(GENES_LENGTH)));
        lifeEnergy -= moveEnergy;
        lifeTime += 1;

        observers.forEach(o -> o.positionChanged(this, oldPosition, position));
    }

    @Override
    public void addObserver(IElementChangeObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IElementChangeObserver observer) {
        observers.remove(observer);
    }

    @Override
    public String toString() {
        return "[" + position + " " + direction + "]";
    }

}
