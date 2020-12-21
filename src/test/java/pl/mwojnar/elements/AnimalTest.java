package pl.mwojnar.elements;

import org.junit.jupiter.api.Test;
import pl.mwojnar.map.IWorldMap;
import pl.mwojnar.map.RectangularMap;
import pl.mwojnar.utils.Vector2d;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    
    private Animal createAnimal(IWorldMap map, Vector2d position) {
        var genes = new Integer[]{ 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4,
                5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7 };
        return new Animal(map, position, 10, Arrays.asList(genes));
    }

    @Test
    void getPosition() {
        var map = new RectangularMap(new Vector2d(5, 5));
        var animal = createAnimal(map, new Vector2d(2, 2));
        var expectedPosition = new Vector2d(2, 2);
        assertEquals(expectedPosition, animal.getPosition());
    }

    @Test
    void illegalPosition() {
        var map = new RectangularMap(new Vector2d(5, 5));
        assertThrows(IllegalArgumentException.class, () -> createAnimal(map, new Vector2d(2, -1)));
    }

    @Test
    void getLifeEnergy() {
        var map = new RectangularMap(new Vector2d(4, 4));
        assertEquals(10, createAnimal(map, new Vector2d(2, 2)).getLifeEnergy());
    }

    @Test
    void addEnergy() {
        var map = new RectangularMap(new Vector2d(4, 4));
        var animal = createAnimal(map, new Vector2d(2, 2));
        animal.addEnergy(5);
        assertEquals(15, animal.getLifeEnergy());
    }

    @Test
    void createNewAnimal() {
        var map = new RectangularMap(new Vector2d(4, 4));
        var animal = createAnimal(map, new Vector2d(2, 2));
        animal.takeEnergyForNewElement();
        assertEquals(8, animal.getLifeEnergy());
    }

    @Test
    void getGenes() {
        var map = new RectangularMap(new Vector2d(4, 4));
        var animal = createAnimal(map, new Vector2d(2, 2));
        var genes = new Integer[]{ 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4,
                5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7 };
        assertEquals(Arrays.asList(genes), animal.getGenes());
    }

    @Test
    void isEdible() {
        var map = new RectangularMap(new Vector2d(4, 4));
        var animal = createAnimal(map, new Vector2d(2, 2));
        assertFalse(animal.isEdible());
    }
}