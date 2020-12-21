package pl.mwojnar.map;

import org.junit.jupiter.api.Test;
import pl.mwojnar.elements.Animal;
import pl.mwojnar.elements.IMapElement;
import pl.mwojnar.utils.Vector2d;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {

    @Test
    void illegalMapSize() {
        assertThrows(IllegalArgumentException.class, () -> new RectangularMap(new Vector2d(-1, 2)));
    }

    @Test
    void legalMapSize() {
        assertDoesNotThrow(() -> new RectangularMap(new Vector2d(4, 4)));
    }

    @Test
    void placeCorrectAnimal() {
        var map = new RectangularMap(new Vector2d(4, 4));
        var genes = new Integer[]{ 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4,
                5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7 };
        assertDoesNotThrow(() -> new Animal(map, new Vector2d(2, 2), 10, Arrays.asList(genes)));
    }

    @Test
    void placeAnimalOutside() {
        var map = new RectangularMap(new Vector2d(4, 4));
        var genes = new Integer[]{ 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4,
                5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7 };
        assertThrows(IllegalArgumentException.class,
                () -> new Animal(map, new Vector2d(2, 20), 10, Arrays.asList(genes)));
    }

    @Test
    void getLowerLeft() {
        var map = new RectangularMap(new Vector2d(10, 5));
        var expected = new Vector2d(0, 0);
        assertEquals(expected, map.getLowerLeft());
    }

    @Test
    void getUpperRight() {
        var map = new RectangularMap(new Vector2d(10, 5));
        var expected = new Vector2d(10, 5);
        assertEquals(expected, map.getUpperRight());
    }

    @Test
    void fitInsideMap() {
        var map = new RectangularMap(new Vector2d(10, 5));
        var expected = new Vector2d(1, 5);
        assertEquals(expected, map.fitInside(new Vector2d(1, 5)));
    }

    @Test
    void fitOutsideMapX() {
        var map = new RectangularMap(new Vector2d(10, 5));
        var expected = new Vector2d(0, 5);
        assertEquals(expected, map.fitInside(new Vector2d(11, 5)));
    }

    @Test
    void fitOutsideMapY() {
        var map = new RectangularMap(new Vector2d(10, 5));
        var expected = new Vector2d(1, 0);
        assertEquals(expected, map.fitInside(new Vector2d(1, 6)));
    }

    @Test
    void fitOutsideMapNegativeX() {
        var map = new RectangularMap(new Vector2d(10, 5));
        var expected = new Vector2d(10, 5);
        assertEquals(expected, map.fitInside(new Vector2d(-1, 5)));
    }

    @Test
    void fitOutsideMapNegativeY() {
        var map = new RectangularMap(new Vector2d(10, 5));
        var expected = new Vector2d(1, 5);
        assertEquals(expected, map.fitInside(new Vector2d(1, -1)));
    }

    @Test
    void isOccupiedFalse() {
        var map = new RectangularMap(new Vector2d(4, 4));
        assertFalse(map.isOccupied(new Vector2d(2, 2)));
    }

    @Test
    void isOccupiedOutOfIndex() {
        var map = new RectangularMap(new Vector2d(4, 4));
        assertThrows(IllegalArgumentException.class, () -> map.isOccupied(new Vector2d(5, 2)));
    }

    @Test
    void isOccupiedTrue() {
        var map = new RectangularMap(new Vector2d(4, 4));
        var genes = new Integer[]{ 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4,
                5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7 };
        new Animal(map, new Vector2d(2, 2), 10, Arrays.asList(genes));
        assertTrue(map.isOccupied(new Vector2d(2, 2)));
    }

    @Test
    void elementsAtEmpty() {
        var map = new RectangularMap(new Vector2d(4, 4));
        var expected = new LinkedList<IMapElement>();
        assertEquals(expected, map.elementsAt(new Vector2d(2, 2)));
    }

    @Test
    void elementsAtOutOfIndex() {
        var map = new RectangularMap(new Vector2d(4, 4));
        assertThrows(IllegalArgumentException.class, () -> map.elementsAt(new Vector2d(5, 2)));
    }

    @Test
    void elementsAtOccupied() {
        var map = new RectangularMap(new Vector2d(4, 4));
        var genes = new Integer[]{ 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4,
                5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7 };
        var animal = new Animal(map, new Vector2d(2, 2), 10, Arrays.asList(genes));
        
        var expected = new LinkedList<IMapElement>();
        expected.add(animal);
        
        assertEquals(expected, map.elementsAt(new Vector2d(2, 2)));
    }

    @Test
    void getElements() {
        var map = new RectangularMap(new Vector2d(4, 4));
        var genes = new Integer[]{ 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4,
                5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7 };
        var animal = new Animal(map, new Vector2d(2, 2), 10, Arrays.asList(genes));

        var expected = new LinkedList<IMapElement>();
        expected.add(animal);

        assertEquals(expected, map.getElements());
    }
}