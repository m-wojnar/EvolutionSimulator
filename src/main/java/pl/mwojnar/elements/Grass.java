package pl.mwojnar.elements;

import pl.mwojnar.utils.Vector2d;

public class Grass implements IMapElement {
    private final Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public boolean isEdible() {
        return true;
    }

    @Override
    public String toString() {
        return position.toString();
    }
}
