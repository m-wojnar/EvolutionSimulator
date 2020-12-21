package pl.mwojnar.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void rotateN() {
        var direction = MapDirection.NORTH;
        var expected = MapDirection.NORTH;
        assertEquals(direction.rotate(0), expected);
    }

    @Test
    void rotateNE() {
        var direction = MapDirection.NORTH;
        var expected = MapDirection.NORTH_EAST;
        assertEquals(direction.rotate(1), expected);
    }

    @Test
    void rotateE() {
        var direction = MapDirection.NORTH;
        var expected = MapDirection.EAST;
        assertEquals(direction.rotate(2), expected);
    }

    @Test
    void rotateSE() {
        var direction = MapDirection.NORTH;
        var expected = MapDirection.SOUTH_EAST;
        assertEquals(direction.rotate(3), expected);
    }

    @Test
    void rotateS() {
        var direction = MapDirection.NORTH;
        var expected = MapDirection.SOUTH;
        assertEquals(direction.rotate(4), expected);
    }

    @Test
    void rotateSW() {
        var direction = MapDirection.NORTH;
        var expected = MapDirection.SOUTH_WEST;
        assertEquals(direction.rotate(5), expected);
    }

    @Test
    void rotateW() {
        var direction = MapDirection.NORTH;
        var expected = MapDirection.WEST;
        assertEquals(direction.rotate(6), expected);
    }

    @Test
    void rotateNW() {
        var direction = MapDirection.NORTH;
        var expected = MapDirection.NORTH_WEST;
        assertEquals(direction.rotate(7), expected);
    }

    @Test
    void rotateN2() {
        var direction = MapDirection.NORTH;
        var expected = MapDirection.NORTH;
        assertEquals(direction.rotate(8), expected);
    }

    @Test
    void toUnitVectorN() {
        var direction = MapDirection.NORTH;
        var expected = new Vector2d(0, 1);
        assertEquals(expected, direction.toUnitVector());
    }

    @Test
    void toUnitVectorNE() {
        var direction = MapDirection.NORTH_EAST;
        var expected = new Vector2d(1, 1);
        assertEquals(expected, direction.toUnitVector());
    }

    @Test
    void toUnitVectorE() {
        var direction = MapDirection.EAST;
        var expected = new Vector2d(1, 0);
        assertEquals(expected, direction.toUnitVector());
    }

    @Test
    void toUnitVectorSE() {
        var direction = MapDirection.SOUTH_EAST;
        var expected = new Vector2d(1, -1);
        assertEquals(expected, direction.toUnitVector());
    }

    @Test
    void toUnitVectorS() {
        var direction = MapDirection.SOUTH;
        var expected = new Vector2d(0, -1);
        assertEquals(expected, direction.toUnitVector());
    }

    @Test
    void toUnitVectorSW() {
        var direction = MapDirection.SOUTH_WEST;
        var expected = new Vector2d(-1, -1);
        assertEquals(expected, direction.toUnitVector());
    }

    @Test
    void toUnitVectorW() {
        var direction = MapDirection.WEST;
        var expected = new Vector2d(-1, 0);
        assertEquals(expected, direction.toUnitVector());
    }

    @Test
    void toUnitVectorNW() {
        var direction = MapDirection.NORTH_WEST;
        var expected = new Vector2d(-1, 1);
        assertEquals(expected, direction.toUnitVector());
    }

    @Test
    void toStringN() {
        assertEquals("N", MapDirection.NORTH.toString());
    }

    @Test
    void toStringNE() {
        assertEquals("NE", MapDirection.NORTH_EAST.toString());
    }

    @Test
    void toStringE() {
        assertEquals("E", MapDirection.EAST.toString());
    }

    @Test
    void toStringSE() {
        assertEquals("SE", MapDirection.SOUTH_EAST.toString());
    }

    @Test
    void toStringS() {
        assertEquals("S", MapDirection.SOUTH.toString());
    }

    @Test
    void toStringSW() {
        assertEquals("SW", MapDirection.SOUTH_WEST.toString());
    }

    @Test
    void toStringW() {
        assertEquals("W", MapDirection.WEST.toString());
    }

    @Test
    void toStringNW() {
        assertEquals("NW", MapDirection.NORTH_WEST.toString());
    }

}