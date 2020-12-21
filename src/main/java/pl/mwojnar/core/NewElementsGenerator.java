package pl.mwojnar.core;

import pl.mwojnar.elements.Animal;
import pl.mwojnar.elements.Grass;
import pl.mwojnar.elements.IMovableElement;
import pl.mwojnar.map.IRandomPositionGiver;
import pl.mwojnar.map.IWorldMap;
import pl.mwojnar.map.RandomPositionGenerator;
import pl.mwojnar.utils.MapDirection;
import pl.mwojnar.utils.Vector2d;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class NewElementsGenerator implements IElementsCreator {

    private static final int ENERGY_TO_CREATE_ANIMAL_RATIO = 2;

    private final Random random;
    private final IWorldMap map;
    private final IRandomPositionGiver positionGiver;
    private final IGenesCreator genesCreator;

    public NewElementsGenerator(IWorldMap map) {
        this.random = new Random();
        this.map = map;
        this.positionGiver = new RandomPositionGenerator(map);
        this.genesCreator = new GenesGenerator();
    }

    public void addFirstAnimals() {
        for (int i = 0; i < Configuration.getStartNumberOfAnimals(); i++)
            new Animal(map, getPositionForNewAnimal(), Configuration.getStartEnergy(), genesCreator.getNewGenes());
    }

    private Vector2d getPositionForNewAnimal() {
        var position = positionGiver.getFreeRandomPosition();

        if (position.isEmpty()) {
            int x = random.nextInt(Configuration.getMapUpperRight().getX() + 1);
            int y = random.nextInt(Configuration.getMapUpperRight().getY() + 1);
            position = Optional.of(new Vector2d(x, y));
        }

        return position.get();
    }

    public void addFirstGrasses() {
        for (int i = 0; i < Configuration.getStartNumberOfGrasses(); i++)
            positionGiver.getFreeRandomPosition().ifPresent(vector2d -> map.place(new Grass(vector2d)));
    }

    public void plantGrasses() {
        // Plant grass inside jungle
        positionGiver.getFreeRandomPosition(Configuration.getJungleLowerLeft(), Configuration.getJungleUpperRight())
                .ifPresent(vector2d -> map.place(new Grass(vector2d)));

        // Plane grass outside jungle
        positionGiver.getFreeRandomPositionOutside(Configuration.getJungleLowerLeft(), Configuration.getJungleUpperRight())
                .ifPresent(vector2d -> map.place(new Grass(vector2d)));
    }

    public void newAnimalsPhase() {
        var allReproducibles = map.getElements().stream()
                .filter(IMovableElement.class::isInstance)
                .map(IMovableElement.class::cast)
                .filter(a -> a.getLifeEnergy() >= Configuration.getStartEnergy() / ENERGY_TO_CREATE_ANIMAL_RATIO)
                .collect(Collectors.toList());

        for (var reproducible : allReproducibles)
            newAnimalsAtPosition(allReproducibles, reproducible.getPosition());
    }

    private void newAnimalsAtPosition(List<IMovableElement> allReproducibles, Vector2d position) {
        var reproducibles = getStrongestReproduciblesAt(allReproducibles, position);

        if (reproducibles.size() >= 2) {
            var takenEnergy = reproducibles.get(0).takeEnergyForNewElement() + reproducibles.get(1).takeEnergyForNewElement();
            var genes = genesCreator.mixGenes(reproducibles.get(0).getGenes(), reproducibles.get(1).getGenes());
            var newPosition = getPositionForNewAnimal(position);

            new Animal(map, newPosition, takenEnergy, genes);
        }
    }

    private List<IMovableElement> getStrongestReproduciblesAt(List<IMovableElement> allReproducibles, Vector2d position) {
        return allReproducibles.stream()
                .filter(r -> r.getPosition().equals(position))
                .sorted(Comparator.comparing(r -> -r.getLifeEnergy()))
                .collect(Collectors.toList());
    }

    private Vector2d getPositionForNewAnimal(Vector2d position) {
        var newPosition = positionGiver.getFreeRandomNeighbour(position);

        if (newPosition.isEmpty()) {
            var randomDirection = MapDirection.NORTH.rotate(random.nextInt(MapDirection.NUMBER_OF_DIRECTIONS));
            newPosition = Optional.of(position.add(randomDirection.toUnitVector()));
        }

        return map.fitInside(newPosition.get());
    }

}
