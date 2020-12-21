package pl.mwojnar.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.mwojnar.utils.Vector2d;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    @BeforeAll
    static void setup() {
        Configuration.createInstance(10,10,5,1,4,3,10,10,10);
    }

    @Test
    void getStartEnergy() {
        assertEquals(5, Configuration.getStartEnergy());
    }

    @Test
    void getMoveEnergy() {
        assertEquals(1, Configuration.getMoveEnergy());
    }

    @Test
    void getPlantEnergy() {
        assertEquals(4, Configuration.getPlantEnergy());
    }

    @Test
    void getStartNumberOfAnimals() {
        assertEquals(10, Configuration.getStartNumberOfAnimals());
    }

    @Test
    void getStartNumberOfGrasses() {
        assertEquals(10, Configuration.getStartNumberOfGrasses());
    }

    @Test
    void getJungleLowerLeft() {
        assertEquals(new Vector2d(3, 3), Configuration.getJungleLowerLeft());
    }

    @Test
    void getJungleUpperRight() {
        assertEquals(new Vector2d(6, 6), Configuration.getJungleUpperRight());
    }

    @Test
    void getMapLowerLeft() {
        assertEquals(new Vector2d(0, 0), Configuration.getMapLowerLeft());
    }

    @Test
    void getMapUpperRight() {
        assertEquals(new Vector2d(9, 9), Configuration.getMapUpperRight());
    }

    @Test
    void getTimeStep() {
        assertEquals(10, Configuration.getTimeStep());
    }
}