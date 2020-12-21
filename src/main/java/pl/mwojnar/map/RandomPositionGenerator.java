package pl.mwojnar.map;

import pl.mwojnar.utils.MapDirection;
import pl.mwojnar.utils.Vector2d;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;

public class RandomPositionGenerator implements IRandomPositionGiver {
    
    private final Random random;
    private final IWorldMap map;

    public RandomPositionGenerator(IWorldMap map) {
        this.random = new Random();
        this.map = map;
    }

    @Override
    public Optional<Vector2d> getFreeRandomPosition() {
        return getFreeRandomPosition(map.getLowerLeft(), map.getUpperRight());
    }

    @Override
    public Optional<Vector2d> getFreeRandomPosition(Vector2d lowerLeft, Vector2d upperRight) {
        if (map.isOutside(lowerLeft) || map.isOutside(upperRight))
            throw new IllegalArgumentException("Random position bounds out of the map");

        int boundX = upperRight.getX() - lowerLeft.getX() + 1;
        int boundY = upperRight.getY() - lowerLeft.getY() + 1;
        return randomPosition(lowerLeft, upperRight, lowerLeft, boundX, boundY, false);
    }

    @Override
    public Optional<Vector2d> getFreeRandomPositionOutside(Vector2d lowerLeft, Vector2d upperRight) {
        if (map.isOutside(lowerLeft) || map.isOutside(upperRight))
            throw new IllegalArgumentException("Random position bounds out of the map");

        int boundX = map.getUpperRight().getX() - map.getLowerLeft().getX() + 1;
        int boundY = map.getUpperRight().getY() - map.getLowerLeft().getY() + 1;
        return randomPosition(lowerLeft, upperRight, map.getLowerLeft(), boundX, boundY, true);
    }

    private Optional<Vector2d> randomPosition(Vector2d lowerLeft, Vector2d upperRight, Vector2d shift,
                                              int boundX, int boundY, boolean isOutside
    ) {
        var checked = new HashSet<Vector2d>();
        int maxIterations = boundX * boundY;
        Vector2d position;

        do {
            int x = random.nextInt(boundX) + shift.getX();
            int y = random.nextInt(boundY) + shift.getY();
            position = new Vector2d(x, y);
            checked.add(position);
        } while (checked.size() < maxIterations && isIncorrectOrOccupied(lowerLeft, upperRight, isOutside, position));

        if (checked.size() == maxIterations)
            return Optional.empty();

        return Optional.of(position);
    }

    private boolean isIncorrectOrOccupied(Vector2d lowerLeft, Vector2d upperRight, boolean isOutside, Vector2d position) {
        if (isOutside)
            return position.follows(lowerLeft) && position.precedes(upperRight) || map.isOccupied(position);
        else
            return map.isOccupied(position);
    }

    @Override
    public Optional<Vector2d> getFreeRandomNeighbour(Vector2d position) {
        var availableNeighbours = new LinkedList<Vector2d>();

        for (var direction : MapDirection.values()) {
            var neighbour = map.fitInside(position.add(direction.toUnitVector()));
            if (!map.isOccupied(neighbour))
                availableNeighbours.add(neighbour);
        }

        if (availableNeighbours.isEmpty())
            return Optional.empty();

        return Optional.of(availableNeighbours.get(random.nextInt(availableNeighbours.size())));
    }
}
