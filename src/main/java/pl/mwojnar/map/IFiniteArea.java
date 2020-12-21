package pl.mwojnar.map;

import pl.mwojnar.utils.Vector2d;

public interface IFiniteArea {
    Vector2d getLowerLeft();
    Vector2d getUpperRight();
    Vector2d fitInside(Vector2d position);
    boolean isOutside(Vector2d position);
}
