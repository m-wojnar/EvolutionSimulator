package pl.mwojnar.map;

import pl.mwojnar.elements.IMapElement;
import pl.mwojnar.elements.IElementChangeObserver;
import pl.mwojnar.utils.Vector2d;

import java.util.List;

public interface IWorldMap extends IFiniteArea, IMapChangedPublisher, IElementChangeObserver {
    void place(IMapElement element);
    void removeDead();
    void moveElements();

    List<IMapElement> getElements();
    List<IMapElement> elementsAt(Vector2d position);
    boolean isOccupied(Vector2d position);
}
