package pl.mwojnar.elements;

import org.junit.jupiter.api.Test;
import pl.mwojnar.utils.Vector2d;

import static org.junit.jupiter.api.Assertions.*;

class GrassTest {

    @Test
    void getPosition() {
        var grass = new Grass(new Vector2d(1, 1));
        var expected = new Vector2d(1, 1);
        assertEquals(expected, grass.getPosition());
    }

    @Test
    void testToString() {
        var grass = new Grass(new Vector2d(1, 1));
        var expected = new Vector2d(1, 1);
        assertEquals(expected.toString(), grass.toString());
    }

    @Test
    void isEdible() {
        assertTrue(new Grass(new Vector2d(2, 2)).isEdible());
    }
}