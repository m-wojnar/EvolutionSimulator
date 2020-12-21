package pl.mwojnar.map;

import pl.mwojnar.elements.IMapElement;
import pl.mwojnar.utils.Vector2d;

public interface IMapChangeObserver {
    void elementAdded(IMapElement element);
    void elementRemoved(IMapElement element);
    void positionChanged(Vector2d oldPosition, Vector2d newPosition);
}
