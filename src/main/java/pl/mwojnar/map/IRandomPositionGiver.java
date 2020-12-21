package pl.mwojnar.map;

import pl.mwojnar.utils.Vector2d;

import java.util.Optional;

public interface IRandomPositionGiver {
    Optional<Vector2d> getFreeRandomPosition();
    Optional<Vector2d> getFreeRandomPosition(Vector2d lowerLeft, Vector2d upperRight);
    Optional<Vector2d> getFreeRandomPositionOutside(Vector2d lowerLeft, Vector2d upperRight);
    Optional<Vector2d> getFreeRandomNeighbour(Vector2d position);
}
