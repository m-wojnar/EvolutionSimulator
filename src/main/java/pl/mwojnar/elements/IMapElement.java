package pl.mwojnar.elements;

import pl.mwojnar.utils.Vector2d;

public interface IMapElement {
    Vector2d getPosition();
    boolean isEdible();
}
