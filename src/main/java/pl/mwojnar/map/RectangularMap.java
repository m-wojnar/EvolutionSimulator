package pl.mwojnar.map;

import pl.mwojnar.core.Configuration;
import pl.mwojnar.elements.IMapElement;
import pl.mwojnar.elements.IMovableElement;
import pl.mwojnar.elements.IElementChangedPublisher;
import pl.mwojnar.utils.Vector2d;

import java.util.*;
import java.util.stream.Collectors;

public class RectangularMap implements IWorldMap {

    private final Map<Vector2d, List<IMapElement>> elementsMap;
    private final List<IMapElement> elementsList;
    private final List<IMapChangeObserver> observers;

    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final int width;
    private final int height;

    public RectangularMap(Vector2d upperRight) {
        if (upperRight.getX() <= 0 || upperRight.getY() <= 0)
            throw new IllegalArgumentException("Width and height of map must be positive numbers");

        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = upperRight;
        this.width = upperRight.getX() + 1;
        this.height = upperRight.getY() + 1;

        this.elementsMap = new HashMap<>();
        this.elementsList = new LinkedList<>();
        this.observers = new LinkedList<>();
    }

    @Override
    public Vector2d getLowerLeft() {
        return lowerLeft;
    }

    @Override
    public Vector2d getUpperRight() {
        return upperRight;
    }

    @Override
    public Vector2d fitInside(Vector2d position) {
        if (isOutside(position))
            return new Vector2d(Math.floorMod(position.getX(), width), Math.floorMod(position.getY(), height));
        else
            return position;
    }

    @Override
    public boolean isOutside(Vector2d position) {
        return !(position.follows(lowerLeft) && position.precedes(upperRight));
    }

    @Override
    public void place(IMapElement element) {
        if (isOutside(element.getPosition()))
            throw new IllegalArgumentException("Element placed out of the map");

        elementsAt(element.getPosition()).add(element);
        elementsList.add(element);
        observers.forEach(o -> o.elementAdded(element));

        if (element instanceof IElementChangedPublisher publisher)
            publisher.addObserver(this);
    }

    @Override
    public void elementRemoved(IMapElement element) {
        elementsAt(element.getPosition()).remove(element);
        elementsList.remove(element);
        observers.forEach(o -> o.elementRemoved(element));

        if (element instanceof IElementChangedPublisher publisher)
            publisher.removeObserver(this);
    }

    @Override
    public void removeDead() {
        var movables = elementsList.stream()
                .filter(IMovableElement.class::isInstance)
                .map(IMovableElement.class::cast)
                .collect(Collectors.toList());

        for (var movable : movables)
            movable.checkState();
    }

    @Override
    public void moveElements() {
        var movables = elementsList.stream()
                .filter(IMovableElement.class::isInstance)
                .map(IMovableElement.class::cast)
                .collect(Collectors.toList());

        for (var movable : movables)
            movable.move(Configuration.getMoveEnergy());
    }

    @Override
    public List<IMapElement> getElements() {
        return elementsList;
    }

    @Override
    public List<IMapElement> elementsAt(Vector2d position) {
        if (isOutside(position))
            throw new IllegalArgumentException("Given position if out of the map");

        return elementsMap.computeIfAbsent(position, k -> new LinkedList<>());
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if (isOutside(position))
            throw new IllegalArgumentException("Given position if out of the map");

        return !elementsAt(position).isEmpty();
    }

    @Override
    public void positionChanged(IMovableElement element, Vector2d oldPosition, Vector2d newPosition) {
        elementsAt(oldPosition).remove(element);
        elementsAt(newPosition).add(element);

        observers.forEach(o -> o.positionChanged(oldPosition, newPosition));
    }

    @Override
    public void addObserver(IMapChangeObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IMapChangeObserver observer) {
        observers.remove(observer);
    }

}
