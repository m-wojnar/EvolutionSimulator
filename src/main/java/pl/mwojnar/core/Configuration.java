package pl.mwojnar.core;

import pl.mwojnar.utils.Vector2d;

import java.util.Objects;

public class Configuration {
    
    public static final String EXCEPTION_MESSAGE = "Configuration accessed before being created";

    private static Integer startEnergy;
    private static Integer moveEnergy;
    private static Integer plantEnergy;
    private static Integer startNumberOfAnimals;
    private static Integer startNumberOfGrasses;
    private static Integer timeStep;

    private static Vector2d jungleLowerLeft;
    private static Vector2d jungleUpperRight;
    private static Vector2d mapLowerLeft;
    private static Vector2d mapUpperRight;

    public static void createInstance(
            int width,
            int height,
            int startEnergy,
            int moveEnergy,
            int plantEnergy,
            int jungleRatio,
            int startNumberOfAnimals,
            int startNumberOfGrasses,
            int timeStep
    ) {
        if (width <= 0 || height <= 0 || startEnergy <= 0 || moveEnergy < 0 || plantEnergy < 0 ||
                jungleRatio <= 0 || startNumberOfAnimals < 0 || startNumberOfGrasses < 0 || timeStep < 0)
            throw new NumberFormatException("Configuration values cannot be negative");

        Configuration.startEnergy = startEnergy;
        Configuration.moveEnergy = moveEnergy;
        Configuration.plantEnergy = plantEnergy;
        Configuration.startNumberOfAnimals = startNumberOfAnimals;
        Configuration.startNumberOfGrasses = startNumberOfGrasses;
        Configuration.timeStep = timeStep;

        int mapUpperRightX = width - 1;
        int mapUpperRightY = height - 1;
        Configuration.mapLowerLeft = new Vector2d(0, 0);
        Configuration.mapUpperRight = new Vector2d(mapUpperRightX, mapUpperRightY);

        int jungleShiftX = (mapUpperRightX - mapUpperRightX / jungleRatio) / 2;
        int jungleShiftY = (mapUpperRightY - mapUpperRightY / jungleRatio) / 2;
        Configuration.jungleLowerLeft =  new Vector2d(jungleShiftX, jungleShiftY);
        Configuration.jungleUpperRight = new Vector2d(mapUpperRightX / jungleRatio, mapUpperRightY / jungleRatio).add(jungleLowerLeft);
    }

    public static int getStartEnergy() {
        Objects.requireNonNull(jungleUpperRight, EXCEPTION_MESSAGE);
        return startEnergy;
    }

    public static int getMoveEnergy() {
        Objects.requireNonNull(moveEnergy, EXCEPTION_MESSAGE);
        return moveEnergy;
    }

    public static int getPlantEnergy() {
        Objects.requireNonNull(plantEnergy, EXCEPTION_MESSAGE);
        return plantEnergy;
    }

    public static int getStartNumberOfAnimals() {
        Objects.requireNonNull(startNumberOfAnimals, EXCEPTION_MESSAGE);
        return startNumberOfAnimals;
    }

    public static int getStartNumberOfGrasses() {
        Objects.requireNonNull(startNumberOfGrasses, EXCEPTION_MESSAGE);
        return startNumberOfGrasses;
    }

    public static Vector2d getJungleLowerLeft() {
        Objects.requireNonNull(jungleLowerLeft, EXCEPTION_MESSAGE);
        return jungleLowerLeft;
    }

    public static Vector2d getJungleUpperRight() {
        Objects.requireNonNull(jungleUpperRight, EXCEPTION_MESSAGE);
        return jungleUpperRight;
    }

    public static Vector2d getMapLowerLeft() {
        Objects.requireNonNull(mapLowerLeft, EXCEPTION_MESSAGE);
        return mapLowerLeft;
    }

    public static Vector2d getMapUpperRight() {
        Objects.requireNonNull(mapUpperRight, EXCEPTION_MESSAGE);
        return mapUpperRight;
    }

    public static int getTimeStep() {
        Objects.requireNonNull(timeStep, EXCEPTION_MESSAGE);
        return timeStep;
    }

    public static int getJungleWidth() {
        Objects.requireNonNull(jungleUpperRight, EXCEPTION_MESSAGE);
        Objects.requireNonNull(jungleLowerLeft, EXCEPTION_MESSAGE);
        return jungleUpperRight.getX() - jungleLowerLeft.getX() + 1;
    }

    public static int getJungleHeight() {
        Objects.requireNonNull(jungleUpperRight, EXCEPTION_MESSAGE);
        Objects.requireNonNull(jungleLowerLeft, EXCEPTION_MESSAGE);
        return jungleUpperRight.getY() - jungleLowerLeft.getY() + 1;
    }

    public static int getWidth() {
        Objects.requireNonNull(mapUpperRight, EXCEPTION_MESSAGE);
        Objects.requireNonNull(mapLowerLeft, EXCEPTION_MESSAGE);
        return mapUpperRight.getX() - mapLowerLeft.getX() + 1;
    }

    public static int getHeight() {
        Objects.requireNonNull(mapUpperRight, EXCEPTION_MESSAGE);
        Objects.requireNonNull(mapLowerLeft, EXCEPTION_MESSAGE);
        return mapUpperRight.getY() - mapLowerLeft.getY() + 1;
    }
}
