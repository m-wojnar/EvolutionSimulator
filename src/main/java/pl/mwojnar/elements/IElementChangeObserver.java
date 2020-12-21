package pl.mwojnar.elements;

import pl.mwojnar.utils.Vector2d;

public interface IElementChangeObserver {
    void positionChanged(IMovableElement element, Vector2d oldPosition, Vector2d newPosition);
    void elementRemoved(IMapElement element);
}
